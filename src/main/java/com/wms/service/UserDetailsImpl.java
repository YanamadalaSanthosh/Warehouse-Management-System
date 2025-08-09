package com.wms.service;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wms.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.*;
public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L;
    private Long id; private String username, email;
    @JsonIgnore private String password;
    private GrantedAuthority authority;
    public UserDetailsImpl(Long id, String u, String e, String p, GrantedAuthority a) {
        this.id=id; this.username=u; this.email=e; this.password=p; this.authority=a;
    }
    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getEmail(), user.getPassword(), new SimpleGrantedAuthority(user.getRole().name()));
    }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() { return Collections.singletonList(authority); }
    public Long getId() { return id; }
    public String getEmail() { return email; }
    @Override public String getPassword() { return password; }
    @Override public String getUsername() { return username; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
