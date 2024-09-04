package dev.mark.factoria_tech_test.encryption;

public interface IEncryptFacade {
    
    String encode(String type, String data);
    String decode(String type, String data);

}