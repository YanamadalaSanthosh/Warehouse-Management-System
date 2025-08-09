package com.wms.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
@Getter @Setter
public class LoginRequest { @NotBlank String username; @NotBlank String password; }
