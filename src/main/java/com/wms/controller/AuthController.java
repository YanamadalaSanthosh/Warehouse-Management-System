package com.wms.controller;
import com.wms.config.JwtUtils;
import com.wms.dto.*;
import com.wms.entity.*;
import com.wms.repository.UserRepository;
import com.wms.service.UserDetailsImpl;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/auth") @Tag(name = "Authentication API")
public class AuthController {
    @Autowired AuthenticationManager authenticationManager;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder encoder;
    @Autowired JwtUtils jwtUtils;
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().map(item -> item.getAuthority()).orElse("UNKNOWN");
        return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), userDetails.getEmail(), role));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) return ResponseEntity.badRequest().body("Error: Username is already taken!");
        if (userRepository.existsByEmail(signUpRequest.getEmail())) return ResponseEntity.badRequest().body("Error: Email is already in use!");
        Role userRole;
        try { userRole = Role.valueOf(signUpRequest.getRole().toUpperCase());
        } catch (IllegalArgumentException e) { return ResponseEntity.badRequest().body("Error: Role not found!"); }
        User user = new User(signUpRequest.getUsername(), signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()), userRole);
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }
}
