package com.csci201finalproject.triptracker.interfaces;

public class ErrorResponseClass {
    private boolean success;
    private String error;
    private String message;

    public ErrorResponseClass(boolean success, String error, String message) {
        this.success = success;
        this.error = error;
        this.message = message;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public String getError() {
        return this.error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
