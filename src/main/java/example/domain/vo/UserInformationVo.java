package com.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户信息展示VO
 */
@Data
@Builder
@Schema(description = "用户个人信息展示视图")
public class UserInformationVo {

    @Schema(description = "用户ID")
    private Long id;

    @Schema(description = "用户昵称")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别（0-未知，1-男，2-女）")
    private Integer gender;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "头像URL")
    private String avatar;

    @Schema(description = "角色权限（admin-管理员，user-普通用户）")
    private String role;

    @Schema(description = "账号创建时间")
    private LocalDateTime createdAt;
}