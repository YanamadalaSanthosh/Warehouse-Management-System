package com.wms.dto;
import lombok.*;
@Getter @Setter
public class JwtResponse {
    private String token, type = "Bearer", username, email, role;
    private Long id;
    public JwtResponse(String token, Long id, String username, String email, String role) {
        this.token=token; this.id=id; this.username=username; this.email=email; this.role=role;
    }
}
