package com.banking.bankingapp.exception;


public class SimpleBankingGlobalException extends RuntimeException {

    private String code;
    private String message;

    public SimpleBankingGlobalException(String message, String errorEntityNotFound) {
        super(message);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
