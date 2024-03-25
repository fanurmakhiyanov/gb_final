package ru.fanur.service.impl;

import ru.fanur.dto.request.LoginPasswordDtoIn;
import ru.fanur.dto.response.LoginPasswordDtoOut;
import ru.fanur.exception.AuthenticationException;
import ru.fanur.exception.JwtFilterAuthException;
import ru.fanur.exception.UserNotFoundException;
import ru.fanur.model.User;
import ru.fanur.repository.UserRepository;
import ru.fanur.security.JwtProvider;
import ru.fanur.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final JwtProvider jwtProvider;

    @Override
    public LoginPasswordDtoOut authenticate(LoginPasswordDtoIn loginPasswordDtoIn) throws AuthenticationException {
        log.info("Try check credentials: login={}", loginPasswordDtoIn.login());
        try {
            var user = userRepository.findByLogin(loginPasswordDtoIn.login())
                    .orElseThrow(UserNotFoundException::new);
            if(user.getPassword().equals(loginPasswordDtoIn.password())) {
                log.info("Try check credentials success");
                return new LoginPasswordDtoOut(jwtProvider.generateAccessToken(loginPasswordDtoIn));
            }
            throw new AuthenticationException();
        } catch (UserNotFoundException e) {
            log.error("User not found");
            throw new AuthenticationException();
        }
    }

    @Override
    public void logout() {
        log.info("Try logout");
        SecurityContextHolder.clearContext();
    }
}