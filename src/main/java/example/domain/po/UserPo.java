package com.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;


/**
 * 用户表
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("user")
@Schema(description="用户表")
public class UserPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "用户ID（雪花算法）")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    @Schema(description = "用户昵称")
    private String username;

    @Schema(description = "手机号")
    private String phone;

    @Schema(description = "加密后的密码")
    private String password;

    @Schema(description = "性别（0-未知，1-男，2-女）")
    private Integer gender;

    @Schema(description = "个性签名")
    private String signature;

    @Schema(description = "用户头像URL")
    private String avatar;

    @Schema(description = "角色权限（admin-管理员，user-普通用户）")
    private String role;

    @Schema(description = "创建时间")
    @TableField(value = "created_at")
    private LocalDateTime createdAt;

    @Schema(description = "更新时间")
    @TableField(value = "updated_at")
    private LocalDateTime updatedAt;


}
