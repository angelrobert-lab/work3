package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "好友申请请求参数")
public class FriendApplyDto {

    @NotNull(message = "申请对象ID不能为空")
    @Schema(description = "申请对象用户ID",requiredMode = Schema.RequiredMode.REQUIRED)
    private Long targetUserId;

    @NotBlank(message = "申请留言不能为空")
    @Schema(description = "申请留言内容", requiredMode = Schema.RequiredMode.REQUIRED)
    private String message;
}