package ru.fanur.controller;

import ru.fanur.dto.response.LoginPasswordDtoOut;
import ru.fanur.dto.request.LoginPasswordDtoIn;
import ru.fanur.exception.AuthenticationException;
import ru.fanur.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Slf4j
@RestController
public class SecurityController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginPasswordDtoOut> authenticate(@RequestBody @Valid LoginPasswordDtoIn loginPassword) throws AuthenticationException {
        log.info("endpoint: POST /login");
        return ResponseEntity
                .ok(authService.authenticate(loginPassword));
    }

    @GetMapping("/login")
    public ResponseEntity<Void> logout(@RequestParam String logout) {
        log.info("endpoint: GET /logout");
        authService.logout();
        return ResponseEntity
                .ok()
                .build();
    }
}