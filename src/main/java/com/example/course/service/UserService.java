package com.example.course.service;

import com.example.course.models.Role;
import com.example.course.models.SecureToken;
import com.example.course.models.User;
import com.example.course.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public User findByUsername(String username) {
        return userDetailsService.findByUsername(username);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean isExistByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isExistByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean hasAuthority(User user, String role) {

        List<String> roleHierarchyNames = List.of("ADMIN", "USER", "GUEST");
        int rolePos = roleHierarchyNames.indexOf(role);

        for (int i = 0; i <= rolePos; i++) {
            if (roleHierarchyNames.get(i).equals(user.getRoles().iterator().next().getName())) {
                return true;
            }
        }
        return false;
    }

    public boolean hasRole(User user, String role) {
        for (Role r : user.getRoles()) {
            if (r.getName().equals(role)) {
                return true;
            }
        }
        return false;
    }

    public void save(User user) {
        userRepository.saveAndFlush(user);
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
        userRepository.flush();
    }

    public void blockById(Long id) {
        User user = findById(id);
        user.setActive(false);
        save(user);
    }

    public void unblockById(Long id) {
        User user = findById(id);
        user.setActive(true);
        save(user);
    }

    public void addRoleById(Long id, Role role) {
        User user = findById(id);
        user.getRoles().add(role);
        save(user);
    }

    public void deleteRoleById(Long id, Role role) {
        User user = findById(id);
        user.getRoles().remove(role);
        save(user);
    }
}
