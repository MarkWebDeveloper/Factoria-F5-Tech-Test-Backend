package dev.mark.factoria_tech_test.encryption;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptEncoder implements IEncoder {

    BCryptPasswordEncoder encoder;

    public BcryptEncoder(BCryptPasswordEncoder encoder) {
        this.encoder = encoder;
    }

    public String encode(String data) {
        String dataEncode = encoder.encode(data);
        return dataEncode;
    }
}
