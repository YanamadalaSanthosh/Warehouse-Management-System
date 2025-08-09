package com.wms.service;
import com.wms.entity.User;
import com.wms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.wms.entity.User;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired UserRepository userRepository;
    @Override @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User Not Found: " + username));
        return UserDetailsImpl.build(user);
    }

// Add this method to the UserDetailsServiceImpl class
public List<User> findAllUsers() {
    return userRepository.findAll();
}
}
