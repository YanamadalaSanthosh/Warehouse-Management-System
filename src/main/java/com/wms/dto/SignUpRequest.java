package com.wms.dto;
import jakarta.validation.constraints.*;
import lombok.*;
@Getter @Setter
public class SignUpRequest {
    @NotBlank @Size(min=3, max=20) String username;
    @NotBlank @Size(max=50) @Email String email;
    @NotBlank @Size(min=6, max=40) String password;
    @NotBlank String role;
}
