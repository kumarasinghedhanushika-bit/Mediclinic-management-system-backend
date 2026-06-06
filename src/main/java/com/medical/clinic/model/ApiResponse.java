package com.medical.clinic.model;

public class ApiResponse<T> {

    private String message;
    private boolean error;
    private boolean success;
    private T data;

    public ApiResponse() {
    }

    public ApiResponse(String message, boolean error, boolean success, T data) {
        this.message = message;
        this.error = error;
        this.success = success;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}