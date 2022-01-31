package com.tbt.trainthebrain.sqlcontroller;

public class ConnectionCheck {
    private boolean successfull;
    private String errorText;

    public ConnectionCheck() {
    }

    public ConnectionCheck(boolean successfull, String errorText) {
        this.successfull = successfull;
        this.errorText = errorText;
    }

    public boolean isSuccessfull() {
        return successfull;
    }

    public String getErrorText() {
        return errorText;
    }

    public void setSuccessfull(boolean successfull) {
        this.successfull = successfull;
    }

    public void setErrorText(String errorText) {
        this.errorText = errorText;
    }
}
