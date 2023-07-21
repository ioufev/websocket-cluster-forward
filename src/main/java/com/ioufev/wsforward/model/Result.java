package com.ioufev.wsforward.model;

import com.ioufev.wsforward.exception.ErrorCode;

public class Result<R> {

    private int code;
    private String message;
    private R data;

    /**
     * 返回成功信息
     *
     * @param <R>
     * @param data 返回数据
     * @return
     */
    public static <R> Result<R> ofSuccess(R data) {
        return new Result<R>()
                .setCode(200)
                .setMessage("success")
                .setData(data);
    }

    /**
     * 返回自定义消息
     *
     * @param <R>
     * @param msg 自定义消息
     * @return
     */
    public static <R> Result<R> ofSuccessMsg(String msg) {
        return new Result<R>()
                .setCode(200)
                .setMessage(msg);
    }

    /**
     * 返回失败信息
     *
     * @param <R>
     * @param code 错误编码
     * @param msg  错误消息
     * @return
     */
    public static <R> Result<R> ofFail(int code, String msg) {
        Result<R> result = new Result<>();
        result.setCode(code);
        result.setMessage(msg);
        return result;
    }

    /**
     * 返回失败信息
     *
     * @param <R>
     * @param errorCode 枚举
     * @return
     */
    public static <R> Result<R> ofFail(ErrorCode errorCode) {
        Result<R> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getDescription());
        return result;
    }

    /**
     * @param <R>
     * @param errorCode
     * @param message
     * @return
     */
    public static <R> Result<R> ofFail(ErrorCode errorCode, String message) {
        Result<R> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMessage(message);
        return result;
    }

    /**
     * @param <R>
     * @param errorCode
     * @param throwable
     * @return
     */
    public static <R> Result<R> ofFail(ErrorCode errorCode, Throwable throwable) {
        Result<R> result = new Result<>();
        result.setCode(errorCode.getCode());
        result.setMessage(errorCode.getDescription());
        return result;
    }

    /**
     * @param <R>
     * @param code
     * @param throwable
     * @return
     */
    public static <R> Result<R> ofThrowable(int code, Throwable throwable) {
        Result<R> result = new Result<>();
        result.setCode(code);
        result.setMessage(throwable.getClass().getName() + ", " + throwable.getMessage());
        return result;
    }

    /**
     * @return
     */
    public int getCode() {
        return code;
    }

    public Result<R> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Result<R> setMessage(String msg) {
        this.message = msg;
        return this;
    }

    public R getData() {
        return data;
    }

    public Result<R> setData(R data) {
        this.data = data;
        return this;
    }

    @Override
    public String toString() {
        return "Result{" +
                " code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

