package dev.mark.factoria_tech_test.users.profiles;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService {

    ProfileRepository repository;

    @PreAuthorize("hasRole('USER')")
    public Profile getById(@NonNull Long id)throws Exception{
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        return profile;
    }
}