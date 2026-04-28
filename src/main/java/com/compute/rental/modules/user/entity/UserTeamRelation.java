package com.compute.rental.modules.user.entity;

import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("user_team_relation")
public class UserTeamRelation {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @TableField("ancestor_user_id")
    private Long ancestorUserId;

    @TableField(exist = false)
    private String ancestorNickname;

    @TableField("descendant_user_id")
    private Long descendantUserId;

    @TableField(exist = false)
    private String descendantNickname;

    @TableField("level_depth")
    private Integer levelDepth;

    @TableField("created_at")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAncestorUserId() {
        return ancestorUserId;
    }

    public void setAncestorUserId(Long ancestorUserId) {
        this.ancestorUserId = ancestorUserId;
    }

    public String getAncestorNickname() {
        return ancestorNickname;
    }

    public void setAncestorNickname(String ancestorNickname) {
        this.ancestorNickname = ancestorNickname;
    }

    public Long getDescendantUserId() {
        return descendantUserId;
    }

    public void setDescendantUserId(Long descendantUserId) {
        this.descendantUserId = descendantUserId;
    }

    public String getDescendantNickname() {
        return descendantNickname;
    }

    public void setDescendantNickname(String descendantNickname) {
        this.descendantNickname = descendantNickname;
    }

    public Integer getLevelDepth() {
        return levelDepth;
    }

    public void setLevelDepth(Integer levelDepth) {
        this.levelDepth = levelDepth;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

}
