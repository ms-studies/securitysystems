package com.mszalek.securitysystems;

public class FormSubmitException extends Exception {
    public FormSubmitException() {
        super();
    }

    public FormSubmitException(String message) {
        super(message);
    }

    public FormSubmitException(String message, Throwable cause) {
        super(message, cause);
    }

    public FormSubmitException(Throwable cause) {
        super(cause);
    }

    protected FormSubmitException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
