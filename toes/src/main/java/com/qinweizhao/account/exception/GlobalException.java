package com.qinweizhao.account.exception;

/**
 * <p>
 *
 * @author Monday_1201
 * @since 2021/3/31 11:21
 * </p>
 */
public class GlobalException extends RuntimeException {

    private String message;

    @Override
    public String getMessage() {
        return message;
    }

    public GlobalException(String message){
        super(message);
        this.message=message;
    }
}
