package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Schema(description = "用户信息修改请求参数")
public class UserInformationDto {

    @Size(min = 2, max = 20, message = "用户昵称长度必须在2-20之间")
    @Schema(description = "用户昵称")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "性别（0-未知，1-男，2-女）", allowableValues = {"0", "1", "2"})
    private Integer gender;

    @Size(max = 100, message = "个性签名不能超过100个字符")
    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "头像URL")
    private String avatar;
}