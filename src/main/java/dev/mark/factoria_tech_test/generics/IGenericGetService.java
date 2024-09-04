package dev.mark.factoria_tech_test.generics;

import org.springframework.lang.NonNull;

public interface IGenericGetService<T> {
    
    T getById(@NonNull Long id) throws Exception;
}