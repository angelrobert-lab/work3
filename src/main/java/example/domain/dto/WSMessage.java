package com.domain.dto;//package com.domain.dto;
//
//import io.swagger.v3.oas.annotations.media.Schema;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.Date;
//
////  这个自己根据需要改
//@Data
//@Builder
//@AllArgsConstructor
//@NoArgsConstructor
//@Schema(description = "WebSocket消息传输对象")
//public class WSMessage {
//
//    @Schema(description = "发送者信息")
//    private UserInfo fromUser;
//
//    @Schema(description = "消息详情")
//    private Message message;
//
//    @Data
//    @Schema(description = "用户信息")
//    public static class UserInfo {
//
//        @Schema(description = "用户ID", example = "1001")
//        private Long userId;
//
//        @Schema(description = "用户昵称", example = "张三")
//        private String userName;
//    }
//
//    @Data
//    @Schema(description = "消息内容")
//    public static class Message {
//
//        @Schema(description = "消息ID", example = "10001")
//        private Long messageId;
//
//        @Schema(description = "发送时间", example = "2023-10-20T10:00:00Z")
//        private Date sendTime;
//
//        @Schema(description = "消息类型", example = "1")
//        private Integer type;
//
//        @Schema(description = "消息内容")
//        private Object body;
//    }
//}
