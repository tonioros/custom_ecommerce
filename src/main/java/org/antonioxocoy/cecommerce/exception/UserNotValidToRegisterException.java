package org.antonioxocoy.cecommerce.exception;

/**
 * User already exist,
 * or password was not valid,
 * or
 */
public class UserNotValidToRegisterException extends Exception {

    public UserNotValidToRegisterException(String message) {
        super(message);
    }
    public UserNotValidToRegisterException(String message, String type) {
        super(message);
        this.type = type;
    }

    private String type;

    public String getType() {
        return type;
    }
}
