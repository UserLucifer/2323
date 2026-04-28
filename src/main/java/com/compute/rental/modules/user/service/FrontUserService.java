package com.compute.rental.modules.user.service;

import com.compute.rental.common.enums.ErrorCode;
import com.compute.rental.common.exception.BusinessException;
import com.compute.rental.modules.user.dto.UserMeResponse;
import com.compute.rental.modules.user.mapper.AppUserMapper;
import org.springframework.stereotype.Service;

@Service
public class FrontUserService {

    private final AppUserMapper appUserMapper;

    public FrontUserService(AppUserMapper appUserMapper) {
        this.appUserMapper = appUserMapper;
    }

    public UserMeResponse getMe(Long id) {
        var user = appUserMapper.selectById(id);
        if (user == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "用户不存在");
        }
        return new UserMeResponse(user.getId(), user.getUserId(), user.getEmail(), user.getNickname(), user.getStatus());
    }
}
