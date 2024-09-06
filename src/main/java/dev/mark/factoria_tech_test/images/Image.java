package dev.mark.factoria_tech_test.images;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;

import dev.mark.factoria_tech_test.users.profiles.Profile;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "images")
public class Image {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    @Column(name = "id_image")
    private Long id;

    @Column(name = "image_name")
    private String imageName;

    @Column(name = "image_title")
    private String imageTitle;

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ManyToMany(mappedBy = "images", cascade = CascadeType.REMOVE)
    Set<Profile> profiles;
}