package com.compute.rental.modules.auth.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.compute.rental.common.enums.CommonStatus;
import com.compute.rental.common.enums.EmailVerifyScene;
import com.compute.rental.common.enums.EmailVerifyStatus;
import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.common.util.DateTimeUtils;
import com.compute.rental.common.util.MoneyUtils;
import com.compute.rental.modules.auth.dto.LoginRequest;
import com.compute.rental.modules.auth.dto.LoginResponse;
import com.compute.rental.modules.auth.dto.ResetPasswordRequest;
import com.compute.rental.modules.auth.dto.SendEmailCodeRequest;
import com.compute.rental.modules.auth.dto.SignupRequest;
import com.compute.rental.modules.auth.dto.VerifyEmailCodeRequest;
import com.compute.rental.modules.auth.support.AuthProperties;
import com.compute.rental.modules.auth.support.VerificationCodeGenerator;
import com.compute.rental.modules.auth.support.VerificationCodeHasher;
import com.compute.rental.modules.user.entity.AppUser;
import com.compute.rental.modules.user.entity.EmailVerifyCode;
import com.compute.rental.modules.user.entity.UserReferralRelation;
import com.compute.rental.modules.user.entity.UserTeamRelation;
import com.compute.rental.modules.user.mapper.AppUserMapper;
import com.compute.rental.modules.user.mapper.EmailVerifyCodeMapper;
import com.compute.rental.modules.user.mapper.UserReferralRelationMapper;
import com.compute.rental.modules.user.mapper.UserTeamRelationMapper;
import com.compute.rental.modules.wallet.entity.UserWallet;
import com.compute.rental.modules.wallet.mapper.UserWalletMapper;
import com.compute.rental.security.jwt.JwtTokenProvider;
import java.time.Duration;
import java.util.Locale;
import java.util.UUID;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class AuthService {

    private static final String LOGIN_ROLE = "USER";
    private static final String CURRENCY_USDT = "USDT";
    private static final String RATE_LIMIT_KEY_PREFIX = "email_code_rate:";

    private final AuthProperties authProperties;
    private final VerificationCodeGenerator codeGenerator;
    private final VerificationCodeHasher codeHasher;
    private final EmailService emailService;
    private final StringRedisTemplate redisTemplate;
    private final EmailVerifyCodeMapper emailVerifyCodeMapper;
    private final AppUserMapper appUserMapper;
    private final UserWalletMapper userWalletMapper;
    private final UserReferralRelationMapper userReferralRelationMapper;
    private final UserTeamRelationMapper userTeamRelationMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public AuthService(
            AuthProperties authProperties,
            VerificationCodeGenerator codeGenerator,
            VerificationCodeHasher codeHasher,
            EmailService emailService,
            StringRedisTemplate redisTemplate,
            EmailVerifyCodeMapper emailVerifyCodeMapper,
            AppUserMapper appUserMapper,
            UserWalletMapper userWalletMapper,
            UserReferralRelationMapper userReferralRelationMapper,
            UserTeamRelationMapper userTeamRelationMapper,
            JwtTokenProvider jwtTokenProvider,
            PasswordEncoder passwordEncoder
    ) {
        this.authProperties = authProperties;
        this.codeGenerator = codeGenerator;
        this.codeHasher = codeHasher;
        this.emailService = emailService;
        this.redisTemplate = redisTemplate;
        this.emailVerifyCodeMapper = emailVerifyCodeMapper;
        this.appUserMapper = appUserMapper;
        this.userWalletMapper = userWalletMapper;
        this.userReferralRelationMapper = userReferralRelationMapper;
        this.userTeamRelationMapper = userTeamRelationMapper;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public void sendSignupEmailCode(SendEmailCodeRequest request, String sendIp) {
        var email = codeHasher.normalizeEmail(request.email());
        if (findUserByEmail(email) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Email is already registered");
        }
        var code = createEmailCode(email, EmailVerifyScene.SIGNUP, sendIp);
        emailService.sendSignupCode(email, code);
    }

    public void verifySignupEmailCode(VerifyEmailCodeRequest request) {
        var email = codeHasher.normalizeEmail(request.email());
        if (findUserByEmail(email) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Email is already registered");
        }
        verifyEmailCode(email, request.code(), EmailVerifyScene.SIGNUP, false);
    }

    public void sendResetPasswordEmailCode(SendEmailCodeRequest request, String sendIp) {
        var email = codeHasher.normalizeEmail(request.email());
        requireEnabledUserByEmail(email);
        var code = createEmailCode(email, EmailVerifyScene.RESET_PASSWORD, sendIp);
        emailService.sendResetPasswordCode(email, code);
    }

    public void verifyResetPasswordEmailCode(VerifyEmailCodeRequest request) {
        var email = codeHasher.normalizeEmail(request.email());
        requireEnabledUserByEmail(email);
        verifyEmailCode(email, request.code(), EmailVerifyScene.RESET_PASSWORD, false);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        var email = codeHasher.normalizeEmail(request.email());
        var user = findUserByEmail(email);
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }
        if (!Integer.valueOf(CommonStatus.ENABLED.value()).equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "User is disabled");
        }
        if (!StringUtils.hasText(user.getPasswordHash())
                || !passwordEncoder.matches(request.password(), user.getPasswordHash())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "Invalid email or password");
        }

        var now = DateTimeUtils.now();
        user.setLastLoginAt(now);
        user.setUpdatedAt(now);
        appUserMapper.updateById(user);

        return buildLoginResponse(user);
    }

    @Transactional
    public LoginResponse signup(SignupRequest request) {
        var email = codeHasher.normalizeEmail(request.email());
        if (findUserByEmail(email) != null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Email is already registered");
        }
        verifyEmailCode(email, request.code(), EmailVerifyScene.SIGNUP, true);
        var user = registerUser(
                email,
                normalizeUsername(request.username()),
                passwordEncoder.encode(request.password()),
                normalizeInviteCode(request.inviteCode())
        );
        user.setLastLoginAt(DateTimeUtils.now());
        appUserMapper.updateById(user);
        return buildLoginResponse(user);
    }

    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        var email = codeHasher.normalizeEmail(request.email());
        var user = requireEnabledUserByEmail(email);
        verifyEmailCode(email, request.code(), EmailVerifyScene.RESET_PASSWORD, true);
        var now = DateTimeUtils.now();
        user.setPasswordHash(passwordEncoder.encode(request.newPassword()));
        user.setUpdatedAt(now);
        appUserMapper.updateById(user);
    }

    private LoginResponse buildLoginResponse(AppUser user) {
        var token = jwtTokenProvider.createAccessToken(user.getId(), user.getUserId(), LOGIN_ROLE);
        return new LoginResponse(
                token,
                "Bearer",
                new LoginResponse.UserProfile(user.getId(), user.getUserId(), user.getEmail(), user.getNickname())
        );
    }

    private String createEmailCode(String email, EmailVerifyScene scene, String sendIp) {
        enforceEmailCodeRateLimit(email, scene);
        var code = codeGenerator.generate(authProperties.codeLength());
        var now = DateTimeUtils.now();

        var verifyCode = new EmailVerifyCode();
        verifyCode.setEmail(email);
        verifyCode.setScene(scene.name());
        verifyCode.setCodeHash(codeHasher.hash(email, scene.name(), code));
        verifyCode.setSendIp(sendIp);
        verifyCode.setExpireAt(now.plus(authProperties.codeTtl()));
        verifyCode.setStatus(EmailVerifyStatus.UNUSED.value());
        verifyCode.setCreatedAt(now);
        emailVerifyCodeMapper.insert(verifyCode);
        return code;
    }

    private void enforceEmailCodeRateLimit(String email, EmailVerifyScene scene) {
        var key = RATE_LIMIT_KEY_PREFIX + email + ":" + scene.name();
        var count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1L) {
            redisTemplate.expire(key, Duration.ofSeconds(60));
        }
        if (count != null && count > authProperties.rateLimitPerMinute()) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUESTS, "Email code send limit exceeded");
        }
    }

    private void verifyEmailCode(String email, String rawCode, EmailVerifyScene scene, boolean consume) {
        var now = DateTimeUtils.now();
        var code = emailVerifyCodeMapper.selectOne(new LambdaQueryWrapper<EmailVerifyCode>()
                .eq(EmailVerifyCode::getEmail, email)
                .eq(EmailVerifyCode::getScene, scene.name())
                .eq(EmailVerifyCode::getStatus, EmailVerifyStatus.UNUSED.value())
                .ge(EmailVerifyCode::getExpireAt, now)
                .orderByDesc(EmailVerifyCode::getId)
                .last("LIMIT 1"));
        if (code == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Invalid or expired email code");
        }
        var expectedHash = codeHasher.hash(email, scene.name(), rawCode);
        if (!expectedHash.equals(code.getCodeHash())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Invalid or expired email code");
        }
        if (!consume) {
            return;
        }
        code.setStatus(EmailVerifyStatus.USED.value());
        code.setUsedAt(now);
        emailVerifyCodeMapper.updateById(code);
    }

    private AppUser registerUser(String email, String username, String passwordHash, String inviteCode) {
        var now = DateTimeUtils.now();
        var parentReferral = findParentReferral(inviteCode);

        var user = new AppUser();
        user.setUserId(generateUserNo());
        user.setEmail(email);
        user.setPasswordHash(passwordHash);
        user.setNickname(username);
        user.setStatus(CommonStatus.ENABLED.value());
        user.setEmailVerifiedAt(now);
        user.setCreatedAt(now);
        user.setUpdatedAt(now);
        appUserMapper.insert(user);

        createWallet(user.getId(), now);
        createReferralRelation(user.getId(), inviteCode, parentReferral, now);
        createTeamRelations(user.getId(), parentReferral, now);
        return user;
    }

    private UserReferralRelation findParentReferral(String inviteCode) {
        if (!StringUtils.hasText(inviteCode)) {
            return null;
        }
        var parent = userReferralRelationMapper.selectOne(new LambdaQueryWrapper<UserReferralRelation>()
                .eq(UserReferralRelation::getInviteCode, inviteCode));
        if (parent == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Invalid invite code");
        }
        return parent;
    }

    private void createWallet(Long userId, java.time.LocalDateTime now) {
        var wallet = new UserWallet();
        wallet.setWalletNo(generateWalletNo());
        wallet.setUserId(userId);
        wallet.setCurrency(CURRENCY_USDT);
        wallet.setAvailableBalance(MoneyUtils.ZERO);
        wallet.setFrozenBalance(MoneyUtils.ZERO);
        wallet.setTotalRecharge(MoneyUtils.ZERO);
        wallet.setTotalWithdraw(MoneyUtils.ZERO);
        wallet.setTotalProfit(MoneyUtils.ZERO);
        wallet.setTotalCommission(MoneyUtils.ZERO);
        wallet.setStatus(CommonStatus.ENABLED.value());
        wallet.setVersionNo(0);
        wallet.setCreatedAt(now);
        wallet.setUpdatedAt(now);
        userWalletMapper.insert(wallet);
    }

    private void createReferralRelation(
            Long userId,
            String parentInviteCode,
            UserReferralRelation parentReferral,
            java.time.LocalDateTime now
    ) {
        var relation = new UserReferralRelation();
        relation.setUserId(userId);
        relation.setInviteCode(generateInviteCode());
        relation.setParentInviteCode(parentInviteCode);
        relation.setCreatedAt(now);
        if (parentReferral != null) {
            relation.setParentUserId(parentReferral.getUserId());
            relation.setLevel1UserId(parentReferral.getUserId());
            relation.setLevel2UserId(parentReferral.getLevel1UserId());
            relation.setLevel3UserId(parentReferral.getLevel2UserId());
        }
        try {
            userReferralRelationMapper.insert(relation);
        } catch (DuplicateKeyException ex) {
            relation.setInviteCode(generateInviteCode());
            userReferralRelationMapper.insert(relation);
        }
    }

    private void createTeamRelations(Long userId, UserReferralRelation parentReferral, java.time.LocalDateTime now) {
        if (parentReferral == null) {
            return;
        }
        insertTeamRelation(parentReferral.getUserId(), userId, 1, now);

        var ancestors = userTeamRelationMapper.selectList(new LambdaQueryWrapper<UserTeamRelation>()
                .eq(UserTeamRelation::getDescendantUserId, parentReferral.getUserId())
                .orderByAsc(UserTeamRelation::getLevelDepth));
        for (var ancestor : ancestors) {
            insertTeamRelation(ancestor.getAncestorUserId(), userId, ancestor.getLevelDepth() + 1, now);
        }
    }

    private void insertTeamRelation(Long ancestorUserId, Long descendantUserId, Integer levelDepth, java.time.LocalDateTime now) {
        var teamRelation = new UserTeamRelation();
        teamRelation.setAncestorUserId(ancestorUserId);
        teamRelation.setDescendantUserId(descendantUserId);
        teamRelation.setLevelDepth(levelDepth);
        teamRelation.setCreatedAt(now);
        userTeamRelationMapper.insert(teamRelation);
    }

    private AppUser findUserByEmail(String email) {
        return appUserMapper.selectOne(new LambdaQueryWrapper<AppUser>().eq(AppUser::getEmail, email));
    }

    private AppUser requireUserByEmail(String email) {
        var user = findUserByEmail(email);
        if (user == null) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "Email is not registered");
        }
        return user;
    }

    private AppUser requireEnabledUserByEmail(String email) {
        var user = requireUserByEmail(email);
        if (!Integer.valueOf(CommonStatus.ENABLED.value()).equals(user.getStatus())) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "User is disabled");
        }
        return user;
    }

    private String normalizeInviteCode(String inviteCode) {
        return StringUtils.hasText(inviteCode) ? inviteCode.trim().toUpperCase(Locale.ROOT) : null;
    }

    private String normalizeUsername(String username) {
        return username.trim();
    }

    private String generateUserNo() {
        return "U" + DateTimeUtils.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
    }

    private String generateWalletNo() {
        return "W" + UUID.randomUUID().toString().replace("-", "").substring(0, 20).toUpperCase(Locale.ROOT);
    }

    private String generateInviteCode() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);
    }
}
