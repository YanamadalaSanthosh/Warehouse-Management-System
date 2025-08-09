package com.wms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List; // Make sure this import is present

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Size(max=100) @Column(unique = true)
    private String username;

    @NotBlank @Size(max=100) @Email @Column(unique = true)
    private String email;

    @NotBlank @Size(max=255)
    private String password;

    @Enumerated(EnumType.STRING) @Column(length=50)
    private Role role;
    
    // This is the new part that fixes the delete issue
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<InventoryLog> inventoryLogs;

    public User(String u, String e, String p, Role r){
        this.username=u;
        this.email=e;
        this.password=p;
        this.role=r;
    }
}