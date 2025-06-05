package com.khaikin.delivery.exception;

import java.io.Serial;

public class ResourceNotFoundException extends RuntimeException {
    @Serial
    private static final long serialVersionUID = 1L;

    String resourceName;
    String field;
    String fieldName;
    Long id;

    public ResourceNotFoundException() {}

    public ResourceNotFoundException(String resourceName, String field, String fieldName) {
        super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
        this.resourceName = resourceName;
        this.field = field;
        this.fieldName = fieldName;
    }

    public ResourceNotFoundException(String resourceName, String field, Long id) {
        super(String.format("%s not found with %s: %s", resourceName, field, id));
        this.resourceName = resourceName;
        this.field = field;
        this.id = id;
    }

}
