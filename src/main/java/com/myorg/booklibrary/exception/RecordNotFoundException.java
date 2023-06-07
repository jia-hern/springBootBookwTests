package com.myorg.booklibrary.exception;

public class RecordNotFoundException extends RuntimeException {
    public RecordNotFoundException(Long readerId, Long bookId) {
        super("The record with reader id: " + readerId + "' and book id: '" + bookId
                + "' does not exist in our records");
    }
}
