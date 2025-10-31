package com.domain.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文章可见性状态枚举
 */
@Getter
@AllArgsConstructor
public enum ArticleVisibilityEnum{
    ONESELF(0, "仅自己可见"),
    PUBLIC(1, "公开"),
    FRIEND_ONLY(2, "仅好友可见");

    @EnumValue
    private Integer value;
    @JsonValue
    private String desc;

}