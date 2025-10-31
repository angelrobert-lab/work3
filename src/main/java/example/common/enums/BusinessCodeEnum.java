package com.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 业务状态码枚举
 * 自定义业务状态码，避免与HTTP状态码冲突
 * 编码规则：按业务模块划分区间，便于维护
 */
@Getter
@AllArgsConstructor
public enum BusinessCodeEnum implements ResultCode {
    // 成功状态 (0 固定表示业务成功)
    SUCCESS(0, "成功"),
    LOGIN_SUCCESS(1,"登陆成功" ),
    ReGISTER_SUCCESS(2,"注册成功" ),
    ADD_ARTICLE_SUCCESS(3,"添加文章成功" ),
    QUERY_ARTICLE_SUCCESS(4,"查询文章成功" ),
    DELETE_ARTICLE_SUCCESS(5,"删除文章成功" ),
    UPDATE_ARTICLE_SUCCESS(6, "更新文章成功"),
    UPLOAD_SUCCESS(7, "上传文件成功"),
    ADD_COMMENT_SUCCESS(8, "添加评论成功"),
    QUERY_COMMENT_SUCCESS(9, "查询评论成功"),

    // 通用业务错误 (1000-1999)：适用于所有模块的通用错误场景
    PARAM_VALIDATION_ERROR(1001, "参数校验失败"),
    OPERATION_TOO_FREQUENT(1002, "操作过于频繁"),
    DATA_NOT_FOUND(1003, "数据不存在"),
    DATA_ALREADY_EXISTS(1004, "数据已存在"),

    // 用户相关错误 (2000-2999)：包含用户登录、授权、Token等场景
    USER_NOT_EXIST(2001, "用户不存在"),
    USER_DISABLED(2002, "用户已被禁用"),
    INVALID_CREDENTIALS(2003, "用户名或密码错误"),
    DUPLICATE_USERNAME(2004, "用户名已存在"),
    CAPTCHA_ERROR(2005, "验证码错误"),
    TOKEN_MISSING(2006, "缺少token，请先登录"),       // 归入用户授权子场景，编码2006
    TOKEN_EXPIRED(2007, "token已过期"),              // 复用原有编码，统一Token相关
    TOKEN_INVALID(2008, "token无效"),                // 复用原有编码，细化无效场景
    TOKEN_REVOKED(2009, "token已被注销，请重新登录"), // 新增编码，补充注销场景
    TOKEN_PERMISSION_DENIED(2010, "token权限不足，无法访问该资源"), // 新增编码，补充权限场景
    TOKEN_EXPIRED_OR_INVALID(2011, "token已过期或无效"), // 新增编码，补充过期或无效场景
    Phone_Is_Registered(2012, "手机号已被注册"),

    //文章评论相关
    COMMENT_NOT_EXIST(4001,"评论不存在"),
    NOT_EXIT_FATHER_COMMENT(4002,"父评论不存在"),
    NO_PERMISSION_TO_DELETE(4003,"没有权限删除评论" ),
    ARTICLE_NOT_EXIST(4004,"文章不存在" ),

    //好友系统相关
    CANNOT_ADD_SELF(400, "不能添加自己为好友"),
    FRIEND_APPLY_EXIST(400, "已发送好友申请，请勿重复发送"),
    ALREADY_FRIENDS(400, "双方已是好友"),
    FRIEND_APPLY_NOT_EXIST(404, "好友申请不存在"),
    FRIEND_APPLY_ALREADY_HANDLED(400, "该申请已处理"),
    NOT_FRIENDS(400, "双方不是好友"),
    FRIEND_APPLY_SEND_SUCCESS(200, "好友申请发送成功"),
    FRIEND_DELETE_SUCCESS(200, "删除好友成功"),
    // 文件相关错误 (3000-3999)：仅用于文件上传、下载等场景
    FILE_UPLOAD_FAILED(3001, "文件上传失败"),
    FILE_TOO_LARGE(3002, "文件大小超限"),
    UNSUPPORTED_FILE_TYPE(3003, "不支持的文件类型"),
    UPLOAD_FILE_EMPTY(3004, "上传文件为空"),
    FILE_FORMAT_ERROR(3005, "文件格式错误"),

    // 系统错误 (9000-9999)：仅用于系统层面的异常，如服务调用失败、配置错误等
    SYSTEM_ERROR(9999, "系统异常");

    private final Integer code;
    private final String message;

}