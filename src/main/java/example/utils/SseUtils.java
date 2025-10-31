package com.utils;//package com.utils;
//
//import com.common.enums.HttpStatusEnum;
//import com.domain.dto.WSMessage;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.MediaType;
//import org.springframework.scheduling.annotation.Async;
//import org.springframework.scheduling.annotation.Scheduled;
//import java.lang.reflect.Field;
//
//import java.io.IOException;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//@Slf4j
//
//public class SseUtils {
//    /**
//     * 容器
//     */
//    private static final ConcurrentHashMap<Long, SseUTF8> SSE_MAP_CACHE = new ConcurrentHashMap<>(0);
//
//    public static SseUTF8 getSseUTFL(Long key) {
//        return SSE_MAP_CACHE.get(key);
//    }
//
//    /**
//     * 默认时长不过期(默认30s)
//     */
//    private static final long DEFAULT_TIMEOUT = 0L;
//
//    /**
//     * @param userId
//     * @Description 创建连接
//     * @Return SseUTF8
//     **/
//    public static SseUTF8 createConnect(Long userId) {
//        SseUTF8 sseEmitter = new SseUTF8(DEFAULT_TIMEOUT);
//
//        // 需要给客户端推送ID
//        if (SSE_MAP_CACHE.containsKey(userId)) {
//            log.info("SSE老用户连接:{}", userId);
//            remove(userId);
//        }
//        // 长链接完成后回调接口(关闭连接时调用)
//        sseEmitter.onCompletion(() -> {
//            log.info("SSE连接结束:{}", userId);
//            remove(userId);
//        });
//        // 连接超时回调
//        sseEmitter.onTimeout(() -> {
//            log.error("SSE连接超时:{}", userId);
//            remove(userId);
//        });
//        // 连接异常时，回调方法
//        sseEmitter.onError(
//                throwable -> {
//                    try {
//                        log.info("SSE{}连接异常,{}", userId, throwable.toString());
//                        sseEmitter.send(SseUTF8.event()
//                                .id(String.valueOf(userId))
//                                .name("发生异常!")
//                                .data("发生异常重试!")
//                                .reconnectTime(3000));
//                        SSE_MAP_CACHE.put(userId, sseEmitter);
//                    } catch (IOException e) {
//                        log.error("用户--->{} SSE连接失败重试,异常信息--->{}", userId, e.getMessage());
//                        e.printStackTrace();
//                    }
//                }
//        );
//        SSE_MAP_CACHE.put(userId, sseEmitter);
//        try {
//            // 注册成功返回用户信息
//            sseEmitter.send(SseUTF8.event().id(String.valueOf(HttpStatusEnum.SUCCESS.getCode())).data(userId, MediaType.APPLICATION_JSON));
//        } catch (IOException e) {
//            log.error("用户--->{} SSE连接失败,异常信息--->{}", userId, e.getMessage());
//        }
//        return sseEmitter;
//    }
//
//    /**
//     * @param userId
//     * @Description 移除用户连接
//     * @Return void
//     **/
//    private static void remove(Long userId) {
//        SSE_MAP_CACHE.get(userId).complete();
//        SSE_MAP_CACHE.remove(userId);
//        log.info("SSE移除用户连接--->{} ", userId);
//    }
//
//    /**
//     * @param userId
//     * @Description 关闭连接
//     * @Return void
//     **/
//    public static void closeConnect(Long userId) {
//        log.info("SSE关闭连接：{}", userId);
//        remove(userId);
//    }
//
//    /**
//     * @param userId, object, seEmitter
//     * @Description 推送信息到客户端
//     * @Return void
//     **/
//    private static <T>boolean sendToClient(Long userId, WSMessage wsMessage, SseUTF8 sseEmitter) {
//        // 推送之前检测心跳是否存在
//        boolean isAlive = checkSseConnectAlive(sseEmitter);
//        WSMessage.UserInfo fromUser = wsMessage.getFromUser();
//        WSMessage.Message message = wsMessage.getMessage();
//        if (!isAlive) {
//            // 失去连接移除
//            log.error("SSE推送消息失败：客户端{}未创建长链接或者关闭,发布者id:{} 失败信息id:{}", userId, fromUser.getUserId(), message.getMessageId());
//            SSE_MAP_CACHE.remove(userId);
//            return false;
//        }
//        SseUTF8.SseEventBuilder sendData = SseUTF8.event().id(String.valueOf(HttpStatusEnum.SUCCESS.getCode())).data(wsMessage, MediaType.APPLICATION_JSON);
//        try {
//            sseEmitter.send(sendData);
//            return true;
//        } catch (IOException e) {
//            log.error("推送消息失败：{}", message);
//        }
//        return true;
//    }
//
//    /**
//     * @param sseEmitter
//     * @Description 检测连接心跳
//     * @Return boolean
//     **/
//    public static boolean checkSseConnectAlive(SseUTF8 sseEmitter) {
//        if (sseEmitter == null) {
//            return false;
//        }
//        // 返回true代表还连接, 返回false代表失去连接
//        return !(Boolean) getField(sseEmitter, sseEmitter.getClass(), "sendFailed") &&
//                !(Boolean) getField(sseEmitter, sseEmitter.getClass(), "complete");
//    }
//
//    /**
//     * @param obj, clazz, fieldName
//     * @Description 反射获取 sendFailed complete
//     * @Return java.lang.Object
//     **/
//    public static Object getField(Object obj, Class<?> clazz, String fieldName) {
//        for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
//            try {
//                Field field;
//                field = clazz.getDeclaredField(fieldName);
//                field.setAccessible(true);
//                return field.get(obj);
//            } catch (Exception ignored) {
//            }
//        }
//
//        return null;
//    }
//
//    /**
//     * @param wsMessage
//     * @Description 发送消息给所有客户端
//     * @Return void
//     **/
//    public static <T>boolean sendTextMessage(WSMessage wsMessage) {
//        if (SSE_MAP_CACHE.isEmpty()) {
//            return false;
//        }
//        if (wsMessage == null) {
//            return false;
//        }
//        boolean isSuccess = false;
//        for (Map.Entry<Long, SseUTF8> entry : SSE_MAP_CACHE.entrySet()) {
//            isSuccess = sendToClient(entry.getKey(), wsMessage, entry.getValue());
//            if (!isSuccess) {
//                log.error("群发客户端{}消息推送,发送者:{},失败消息id:{}", entry.getKey(), wsMessage.getFromUser().getUserId(), wsMessage.getMessage().getMessageId());
//            }
//        }
//        return isSuccess;
//    }
//
//    /**
//     * @param clientId, object
//     * @Description 给指定客户端发送消息
//     * @Return Boolean
//     **/
//    public static <T>boolean sendTextMessage(Long clientId, WSMessage wsMessage) {
//        return sendToClient(clientId, wsMessage, SSE_MAP_CACHE.get(clientId));
//    }
//
//    /**
//     * @param live
//     * @Description 发送消息检测心跳
//     * @Return void
//     **/
//    public static <T>boolean sendTextMessageCheck(String live) {
//        if (SSE_MAP_CACHE.isEmpty()) {
//            return false;
//        }
//        if (live == null) {
//            return false;
//        }
//        boolean isSuccess = false;
//        for (Map.Entry<Long, SseUTF8> entry : SSE_MAP_CACHE.entrySet()) {
//            isSuccess = sendToClientCheck(entry.getKey(), live, entry.getValue());
//            if (!isSuccess) {
//                log.error("群发客户端{}消息推送,心跳检查,失败消息:{}", entry.getKey(), live);
//            }
//        }
//        return isSuccess;
//    }
//
//    /**
//     * @param userId, live, seEmitter
//     * @Description 推送信息到客户端（检查）
//     * @Return void
//     **/
//    private static <T>boolean sendToClientCheck(Long userId, String live, SseUTF8 sseEmitter) {
//        // 推送之前检测心跳是否存在
//        boolean isAlive = checkSseConnectAlive(sseEmitter);
//        if (!isAlive) {
//            // 失去连接移除
//            log.error("SSE推送消息失败：客户端{}未创建长链接或者关闭,失败信息:{}", userId, live);
//            SSE_MAP_CACHE.remove(userId);
//            return false;
//        }
//        SseUTF8.SseEventBuilder sendData = SseUTF8.event().id(String.valueOf(HttpStatusEnum.SUCCESS.getCode())).data(live, MediaType.APPLICATION_JSON);
//        try {
//            sseEmitter.send(sendData);
//            return true;
//        } catch (IOException e) {
//            log.error("推送消息失败：{}", live);
//        }
//        return true;
//    }
//
//    /**
//     * @Description 检测客户端心跳（连接状态，给客户端发送信息，如果sendFailed,complete返回false 移除客户端，说明客户端关闭）
//     * @param
//     * @Return void
//     **/
//    @Async
//    @Scheduled(cron = "0 0/2592000 * * * ?")
//    public void checkSseAlive() {
//        log.info("检测客户端连接状态");
//        sendTextMessageCheck("LIVE");
//    }
//
//
//}
