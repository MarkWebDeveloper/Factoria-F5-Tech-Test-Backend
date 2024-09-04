package dev.mark.factoria_tech_test.users.profiles;

public class ProfileNotFoundException extends ProfileException{

    public ProfileNotFoundException(String message) {
        super(message);
    }

    public ProfileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}