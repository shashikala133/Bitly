package com.url.shortener.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String email;
    private String role="ROLE_USER";
    @OneToMany(mappedBy = "user")
    private List<UrlMapping> urlMappingList;
}
