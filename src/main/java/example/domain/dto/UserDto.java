package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.mapstruct.Mapping;

@Data
@Schema(description = "用户基本信息DTO")
public class UserDto {

    @Schema(description = "用户ID")
    private Long id;

    @Size(min = 2, max = 20, message = "用户名长度必须在2-20之间")
    @Schema(description = "用户名")
    private String username;

    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "密码（加密存储）")
    private String password;

    @Schema(description = "用户角色", allowableValues = {"admin", "user"})
    private String role;
}