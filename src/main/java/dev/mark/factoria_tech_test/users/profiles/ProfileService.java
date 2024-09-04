package dev.mark.factoria_tech_test.users.profiles;

import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import dev.mark.factoria_tech_test.generics.IGenericGetService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProfileService implements IGenericGetService<Profile> {

    ProfileRepository repository;

    @PreAuthorize("hasRole('USER')")
    public Profile getById(@NonNull Long id)throws Exception{
        Profile profile = repository.findById(id).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));

        return profile;
    }

    @PreAuthorize("hasRole('USER')")
    public Profile getByEmail(@NonNull String email)throws Exception{
        Profile profile = repository.findByEmail(email).orElseThrow(() -> new ProfileNotFoundException("Profile not found"));
        return profile;
    }
    
}