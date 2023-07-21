package com.ioufev.wsforward.exception;

public enum WebSocketErrorCode implements ErrorCode {

    YOU_ARE_WRONG(40001, "你错了！"),
    PARAMETER_FORMAT_ERROR(40002, "计算参数格式错误！"),
    ;

    private final int code;
    private final String description;

    WebSocketErrorCode(int code, String description) {
        this.code = code;
        this.description = description;
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public String toString() {
        return String.format("Code:[%s], Description:[%s].", this.code,
                this.description);
    }
}
