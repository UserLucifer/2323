package com.compute.rental.modules.user.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_referral_relation")
public class UserReferralRelation {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("user_id")
    private Long userId;

    @TableField("invite_code")
    private String inviteCode;

    @TableField("parent_user_id")
    private Long parentUserId;

    @TableField("parent_invite_code")
    private String parentInviteCode;

    @TableField("level1_user_id")
    private Long level1UserId;

    @TableField("level2_user_id")
    private Long level2UserId;

    @TableField("level3_user_id")
    private Long level3UserId;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getInviteCode() {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode) {
        this.inviteCode = inviteCode;
    }

    public Long getParentUserId() {
        return parentUserId;
    }

    public void setParentUserId(Long parentUserId) {
        this.parentUserId = parentUserId;
    }

    public String getParentInviteCode() {
        return parentInviteCode;
    }

    public void setParentInviteCode(String parentInviteCode) {
        this.parentInviteCode = parentInviteCode;
    }

    public Long getLevel1UserId() {
        return level1UserId;
    }

    public void setLevel1UserId(Long level1UserId) {
        this.level1UserId = level1UserId;
    }

    public Long getLevel2UserId() {
        return level2UserId;
    }

    public void setLevel2UserId(Long level2UserId) {
        this.level2UserId = level2UserId;
    }

    public Long getLevel3UserId() {
        return level3UserId;
    }

    public void setLevel3UserId(Long level3UserId) {
        this.level3UserId = level3UserId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
