package com.qinweizhao.account.exception;

import com.qinweizhao.account.utils.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/31 11:24
 * </p>
 */
@ControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(GlobalException.class)
    public Result GlobalException(GlobalException e){
        return Result.error(e.getMessage());
    }
}
