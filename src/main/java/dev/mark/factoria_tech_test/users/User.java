package dev.mark.factoria_tech_test.users;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.mark.factoria_tech_test.users.profiles.Profile;
import dev.mark.factoria_tech_test.users.roles.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_user")
    private Long id;

    private String username;
    private String password;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
    private Profile profile;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "role_users", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id_user"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id_role"))
    Set<Role> roles;

    public User(String username, String password) { 
        this.username = username; 
        this.password = password; 
    } 
}
