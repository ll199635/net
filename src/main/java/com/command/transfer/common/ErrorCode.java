package com.command.transfer.common;

public enum ErrorCode {
    SUCCESS(0, "success"),
    PARSE_FAIL(5000, "网络错误"),
    ARGUMENT_ILLEGAL(1000, "参数错误"),
    ARGUMENT_IS_BLANK(1001, "参数为空，请检查输入"),
    ARGUMENT_TYPE_ERROR(1002, "参数类型错误，请检查输入"),
    ARGUMENT_NOT_VALID(1003, "参数校验错误，请检查输入"),
    ;

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
