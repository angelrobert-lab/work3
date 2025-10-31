package com.domain.dto;

import com.domain.enums.FriendStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "好友申请处理请求参数")
public class FriendOperateDto {

    @NotNull(message = "申请ID不能为空")
    @Schema(description = "好友申请记录ID",requiredMode = Schema.RequiredMode.REQUIRED)
    private Long applyId;

    @NotNull(message = "处理状态不能为空")
    @Schema(description = "处理状态（1-同意，2-拒绝）",
            requiredMode = Schema.RequiredMode.REQUIRED,
            allowableValues = {"1", "2"})
    private FriendStatusEnum status;
}