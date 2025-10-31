package com.domain.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 好友信息展示VO
 */
@Data
@Builder
@Schema(description = "好友信息展示VO")
public class FriendVo {

    @Schema(description = "好友ID")
    private Long friendId;

    @Schema(description = "好友昵称")
    private String username;

    @Schema(description = "好友头像URL")
    private String avatar;

    @Schema(description = "好友个性签名")
    private String signature;

    @Schema(description = "备注名（可为空）")
    private String remark;

    @Schema(description = "成为好友的时间")
    private LocalDateTime confirmTime;

    @Schema(description = "好友性别（0-未知，1-男，2-女）")
    private Integer gender;
}