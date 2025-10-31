package com.domain.vo;

import com.domain.enums.FriendStatusEnum;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public class FriendApplyVo {
    private static final long serialVersionUID = 1L;
    @Schema(description = "申请人ID")
    private Long applyUserId;
    @Schema(description = "申请留言")
    private String message;
    @Schema(description = "申请时间")
    private LocalDateTime applyTime;
    @Schema(description = "申请状态0-未处理 1-已同意 2-已拒绝")
    private FriendStatusEnum status;
}
