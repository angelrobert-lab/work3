
package com.domain.po;

import com.common.enums.HttpStatusEnum;
import com.common.enums.ResultCode;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 自定义异常
 */
@Data
@Builder
@NoArgsConstructor
public class EIException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private String msg;
    private int code = 500;

    public EIException(String msg) {
        super(msg);
        this.msg = msg;
    }

    public EIException(String msg, Throwable e) {
        super(msg, e);
        this.msg = msg;
    }

    public EIException(String msg, int code) {
        super(msg);
        this.msg = msg;
        this.code = code;
    }
    public EIException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.msg = resultCode.getMessage();
        this.code = resultCode.getCode();
    }

    public EIException(String msg, int code, Throwable e) {
        super(msg, e);
        this.msg = msg;
        this.code = code;
    }

}
