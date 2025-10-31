package com.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "好友信息DTO")
public class FriendDto {

    @Schema(description = "好友用户ID")
    private Long friendId;

    @Schema(description = "好友昵称")
    private String username;

    @Schema(description = "好友头像URL")
    private String avatar;

    @Schema(description = "好友备注名")
    private String remark;

    @Schema(description = "成为好友的时间")
    private LocalDateTime becomeFriendTime;
}