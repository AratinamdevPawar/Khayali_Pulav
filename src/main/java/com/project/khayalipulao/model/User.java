package com.project.khayalipulao.model;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;
    private String phone;
    private String email;
    private String password;
    private String role;

    // Store profile photo as binary data
    @Lob
    @Column(name = "profile_photo", columnDefinition = "LONGBLOB")
    private byte[] profileImage;
    
    // Store content type of the image (e.g., "image/jpeg", "image/png")
    @Column(name = "profile_photo_type")
    private String profileImageContentType;
    
    private String city;
    private String pinCode;

    @OneToMany(mappedBy = "provider", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems = new ArrayList<>();
}