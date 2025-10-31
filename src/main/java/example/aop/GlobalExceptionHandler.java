package com.aop;

import cn.dev33.satoken.exception.*;
import cn.hutool.http.HttpStatus;
import com.common.enums.BusinessCodeEnum;
import com.common.enums.HttpStatusEnum;
import com.domain.po.EIException;
import com.utils.ResponseResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;

import jakarta.servlet.http.HttpServletRequest;
import java.nio.file.AccessDeniedException;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Sa-Token 未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseResult<?> handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String errorMsg;
        int code;
        switch (e.getType()) {
            case NotLoginException.TOKEN_TIMEOUT_MESSAGE:
                errorMsg = "登录已过期，请重新登录";
                code = BusinessCodeEnum.TOKEN_EXPIRED.getCode();
                break;
            case NotLoginException.NOT_TOKEN_MESSAGE:
                errorMsg = "无效的令牌，请重新登录";
                code = BusinessCodeEnum.TOKEN_INVALID.getCode();
                break;
            case NotLoginException.TOKEN_FREEZE_MESSAGE:
                errorMsg = "令牌已被封禁，请联系管理员";
                code = BusinessCodeEnum.TOKEN_PERMISSION_DENIED.getCode();
                break;
            default:
                errorMsg = "登录状态异常，请重新登录";
                code = HttpStatus.HTTP_UNAUTHORIZED;
        }

        log.error("请求地址'{}',认证失败: {}", requestURI, e.getMessage());
        return ResponseResult.fail(code, errorMsg);
    }

    /**
     * Sa-Token 权限不足异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ResponseResult<?> handleNotPermissionException(NotPermissionException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // 记录缺失的权限码，便于调试
        log.error("请求地址'{}',缺失权限: {}", requestURI, e.getPermission());
        // 给用户的提示隐藏具体权限码，避免信息泄露
        return ResponseResult.fail(
                HttpStatus.HTTP_FORBIDDEN,
                "没有操作权限，请联系管理员开通：" + e.getPermission().split(":")[0] + "模块权限"
        );
    }

    /**
     * Sa-Token 角色不足异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ResponseResult<?> handleNotRoleException(NotRoleException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',缺失角色: {}", requestURI, e.getRole());
        return ResponseResult.fail(
                HttpStatus.HTTP_FORBIDDEN,
                "没有访问权限，需要[" + e.getRole() + "]角色"
        );
    }

    /**
     * Sa-Token 其他异常
     */
    @ExceptionHandler(SaTokenException.class)
    public ResponseResult<?> handleSaTokenException(SaTokenException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',Sa-Token异常: {}", requestURI, e.getMessage(), e);
        String errorMsg = "身份验证异常，请刷新页面重试";
        return ResponseResult.fail(BusinessCodeEnum.TOKEN_INVALID.getCode(), errorMsg);
    }
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseResult handleAccessDeniedException(AccessDeniedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',权限校验失败'{}'", requestURI, e.getMessage());
        return ResponseResult.fail(HttpStatusEnum.FORBIDDEN.getCode(), HttpStatusEnum.FORBIDDEN.getMessage());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseResult handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',不支持'{}'请求", requestURI, e.getMethod());
        return ResponseResult.fail(e.getMessage());
    }

    @ExceptionHandler(EIException.class)
    public ResponseResult handleServiceException(EIException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        Integer code = e.getCode();
        return code != null ? ResponseResult.fail(code, e.getMessage()) : ResponseResult.fail(e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseResult handleRuntimeException(RuntimeException e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生未知异常.", requestURI, e);
        return ResponseResult.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseResult handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        log.error("请求地址'{}',发生系统异常.", requestURI, e);
        return ResponseResult.fail(e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    public ResponseResult handleBindException(BindException e) {
        log.error(e.getMessage(), e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return ResponseResult.fail(message);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        String message = e.getBindingResult().getFieldError().getDefaultMessage();
        return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(), "参数校验异常：" + message);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseResult handleMaxUploadSizeExceededException(MaxUploadSizeExceededException e) {
        log.error(e.getMessage(), e);
        return ResponseResult.fail(HttpStatusEnum.BAD_REQUEST.getCode(), "文件上传过大");
    }

    /**
     * 补充：404异常处理
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseResult handleNoHandlerFoundException(NoHandlerFoundException e, HttpServletRequest request) {
        log.error("请求地址'{}'不存在", request.getRequestURI());
        return ResponseResult.fail(HttpStatus.HTTP_NOT_FOUND, "请求的接口不存在");
    }
}