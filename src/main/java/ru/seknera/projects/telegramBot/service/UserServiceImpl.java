package ru.seknera.projects.telegramBot.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.seknera.projects.telegramBot.exceptions.UsernameAlreadyExistsException;
import ru.seknera.projects.telegramBot.model.User;
import ru.seknera.projects.telegramBot.model.UserAuthority;
import ru.seknera.projects.telegramBot.model.UserRole;
import ru.seknera.projects.telegramBot.repository.UserRepository;
import ru.seknera.projects.telegramBot.repository.UserRolesRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRolesRepository userRolesRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EntityManager manager;

    @Transactional
    @Override
    public void registration(String username, String password) {
        if (userRepository.findByUsername(username).isEmpty()) {
            User user = userRepository.save(
                    new User()
                            .setId(null)
                            .setUsername(username)
                            .setPassword(passwordEncoder.encode(password))
                            .setLocked(false)
                            .setExpired(false)
                            .setEnabled(true)
            );
            userRolesRepository.save(new UserRole(null, UserAuthority.USER, user));
        } else {
            throw new UsernameAlreadyExistsException();
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @Transactional
    @Override
    public List<UserRole> getUserRoles(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        return user.getUserRoles();
    }

    @Transactional
    @Override
    public void addRoleToUser(String username, UserAuthority role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        UserRole userRole = new UserRole(null, role, user);
        userRolesRepository.save(userRole);
    }

    @Transactional
    @Override
    public void removeRoleFromUser(String username, UserAuthority role) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));
        Optional<UserRole> userRole = user.getUserRoles().stream()
                .filter(r -> r.getUserAuthority().equals(role))
                .findFirst();
        userRole.ifPresent(userRolesRepository::delete);
    }
}
