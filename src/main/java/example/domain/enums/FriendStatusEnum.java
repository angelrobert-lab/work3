package com.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 好友关系状态枚举
 */
@Getter
@AllArgsConstructor
public enum FriendStatusEnum {
    PENDING(0, "待验证"),
    CONFIRMED(1, "已成为好友"),
    REJECTED(2, "已拒绝");

    @EnumValue
    private final Integer value;

    @JsonValue
    private final String desc;
}