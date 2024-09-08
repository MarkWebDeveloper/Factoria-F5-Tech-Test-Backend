package dev.mark.factoria_tech_test.users;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import dev.mark.factoria_tech_test.users.security.SecurityUser;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UsersManager {

    public Long getCurrentUserId() {
        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();
        Long principalId = 0L;

        if (auth.getPrincipal() instanceof SecurityUser securityUser) {
            principalId = securityUser.getId();
            System.out.println("The user id is:" + principalId);
        }

        return principalId;
    }
}
