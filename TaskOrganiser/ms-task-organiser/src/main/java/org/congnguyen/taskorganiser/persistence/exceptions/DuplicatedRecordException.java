package org.congnguyen.taskorganiser.persistence.exceptions;

public class DuplicatedRecordException extends Exception {
    public DuplicatedRecordException(String message) {
        super(message);
    }
}
