package dev.mark.factoria_tech_test.users.register;

import java.util.HashSet;
import java.util.Set;
import org.springframework.stereotype.Service;

import dev.mark.factoria_tech_test.encryption.EncoderFacade;
import dev.mark.factoria_tech_test.users.User;
import dev.mark.factoria_tech_test.users.UserRepository;
import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.profiles.ProfileRepository;
import dev.mark.factoria_tech_test.users.roles.Role;
import dev.mark.factoria_tech_test.users.roles.RoleService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RegisterService {

    UserRepository userRepository;
    RoleService roleService;
    EncoderFacade encoder;
    ProfileRepository profileRepository;

    public String createUser(SignUpDTO signupDTO) {

        User newUser = new User(signupDTO.getUsername(), signupDTO.getPassword()); 

        String passwordDecoded = encoder.decode("base64", newUser.getPassword());
        String passwordEncoded = encoder.encode("bcrypt", passwordDecoded);
        
        newUser.setPassword(passwordEncoded);
        assignDefaultRole(newUser);

        userRepository.save(newUser);

        User savedUser = userRepository.getReferenceById(newUser.getId());

        Profile newProfile = Profile.builder()
                .id(savedUser.getId())
                .user(savedUser)
                .build();

        profileRepository.save(newProfile);

        String message = "User with the username " + newUser.getUsername() + " is successfully created.";

        return message;

    }

    public void assignDefaultRole(User user) {

        Role defaultRole = roleService.getById(2L);
        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);

        user.setRoles(roles);
    }
}