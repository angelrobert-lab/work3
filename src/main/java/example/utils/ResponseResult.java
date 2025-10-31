package com.utils;

import com.common.enums.BusinessCodeEnum;
import com.common.enums.HttpStatusEnum;
import com.common.enums.ResultCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 返回结果集
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseResult<T> {

    private Integer code;

    private String message;

    private T data;

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    返回数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    private static <T> ResponseResult<T> response(Integer code, String message, T data) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        responseResult.setData(data);
        return responseResult;
    }

    /**
     * 全参数方法
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    private static <T> ResponseResult<T> response(Integer code, String message) {
        ResponseResult<T> responseResult = new ResponseResult<>();
        responseResult.setCode(code);
        responseResult.setMessage(message);
        return responseResult;
    }

    /**
     * 成功返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success() {
        return response(HttpStatusEnum.SUCCESS.getCode(), HttpStatusEnum.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回（枚举参数）
     *
     * @param resultCode 枚举参数
     * @param <T>              泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(ResultCode resultCode) {
        return response(resultCode.getCode(), resultCode.getMessage());
    }
    public static <T> ResponseResult<T> success(ResultCode resultCode,T data) {
        return response(resultCode.getCode(), resultCode.getMessage(), data);
    }

    /**
     * 成功返回（数据）
     *
     * @param data 数据
     * @param <T>  泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), HttpStatusEnum.SUCCESS.getMessage(), data);
    }

    /**
     * 成功返回（返回信息 + 数据）
     *
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(String message, T data) {
        return response(HttpStatusEnum.SUCCESS.getCode(), message, data);
    }

    /**
     * 成功返回（状态码+返回信息+数据）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param data    数据
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(Integer code, String message, T data) {
        return response(code, message, data);
    }

    /**
     * 成功返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> success(String message) {
        return response(HttpStatusEnum.SUCCESS.getCode(), message, null);
    }

    /**
     * 失败返回（无参）
     *
     * @param <T> 泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail() {
        return response(HttpStatusEnum.ERROR.getCode(), HttpStatusEnum.ERROR.getMessage(), null);
    }

    /**
     * 失败返回（枚举）
     *
     * @param resultCode 枚举
     * @param <T>              泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(ResultCode resultCode) {
        return response(resultCode.getCode(), resultCode.getMessage());
    }
    public static <T> ResponseResult<T> fail(ResultCode resultCode,T data) {
        return response(resultCode.getCode(), resultCode.getMessage(),data);
    }

    /**
     * 失败返回（状态码+返回信息）
     *
     * @param code    状态码
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(Integer code, String message) {
        return response(code, message);
    }

    /**
     * 失败返回（返回信息）
     *
     * @param message 返回信息
     * @param <T>     泛型
     * @return {@link ResponseResult<T>}
     */
    public static <T> ResponseResult<T> fail(String message) {
        return response(HttpStatusEnum.ERROR.getCode(), message, null);
    }
}