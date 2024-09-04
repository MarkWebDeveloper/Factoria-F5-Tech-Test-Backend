package dev.mark.factoria_tech_test.users.roles;

public class RoleNotFoundException extends RoleException {

    public RoleNotFoundException(String message) {
        super(message);
    }

    public RoleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
    
}