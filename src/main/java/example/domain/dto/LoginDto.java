package com.domain.dto;

import com.annotation.AtLeastOneNotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AtLeastOneNotNull(fields = {"username", "phone"}, message = "用户名和手机号不能同时为空")
@Schema(description = "用户登录请求参数")
public class LoginDto {

    @Schema(description = "用户名（与手机号二选一）")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号（与用户名二选一）")
    private String phone;

    @NotBlank(message = "密码不能为空")
    @Schema(description = "登录密码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^\\d{6}$", message = "验证码必须为6位数字")
    @Schema(description = "登录验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    private String captcha;
}