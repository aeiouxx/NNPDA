package com.josefy.nnpda.infrastructure.exceptions;

public class NotFoundException extends RuntimeException {
    private Class<?> Type;
    public NotFoundException(Class<?> type) {
        super("Entity of type " + type.getName() + " not found");
        Type = type;
    }
    public static NotFoundException create(Class<?> type) {
        return new NotFoundException(type);
    }
}
