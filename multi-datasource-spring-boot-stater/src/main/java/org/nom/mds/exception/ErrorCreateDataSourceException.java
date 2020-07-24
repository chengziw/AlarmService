package org.nom.mds.exception;

public class ErrorCreateDataSourceException extends RuntimeException {

    public ErrorCreateDataSourceException(String message) {
        super(message);
    }

    public ErrorCreateDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }
}
