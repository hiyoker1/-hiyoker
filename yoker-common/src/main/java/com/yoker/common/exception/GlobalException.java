package com.yoker.common.exception;

import com.yoker.common.result.ResultCode;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final Integer code;

    public GlobalException(String message) {
        super(message);
        this.code = ResultCode.ERROR.getCode();
    }

    public GlobalException(ResultCode resultCode) {
        super(resultCode.getMsg());
        this.code = resultCode.getCode();
    }
}