package ru.seknera.projects.telegramBot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.seknera.projects.telegramBot.model.UserAuthority;
import ru.seknera.projects.telegramBot.model.UserRole;
import ru.seknera.projects.telegramBot.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/role")
public class UserRoleController {

    private final UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<List<UserRole>> getUserRoles(@PathVariable String username) {
        List<UserRole> roles = userService.getUserRoles(username);
        return ResponseEntity.ok(roles);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addRoleToUser(
            @RequestParam("username") String username,
            @RequestParam("role") UserAuthority role) {
        userService.addRoleToUser(username, role);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update")
    public ResponseEntity<Void> removeRoleFromUser(
            @RequestParam("username") String username,
            @RequestParam("role") UserAuthority role) {
        userService.removeRoleFromUser(username, role);
        return ResponseEntity.ok().build();
    }
}