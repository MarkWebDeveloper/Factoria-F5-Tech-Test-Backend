package dev.mark.factoria_tech_test.users.profiles;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping(path = "${api-endpoint}")
public class ProfileController {
    
    ProfileService service;

    @PostAuthorize("returnObject.body.id == authentication.principal.id")
    @GetMapping(path = "/user/profiles/getById/{id}")
    public ResponseEntity<Profile> getById(@NonNull @PathVariable("id") Long id) throws Exception{
        Profile profile = service.getById(id);
        return ResponseEntity.status(HttpStatusCode.valueOf(200)).body(profile);
    }
    
}