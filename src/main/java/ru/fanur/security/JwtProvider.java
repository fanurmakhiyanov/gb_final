package ru.fanur.security;

import ru.fanur.dto.request.LoginPasswordDtoIn;

public interface JwtProvider {
    boolean validateAccessToken(String token);
    String generateAccessToken(LoginPasswordDtoIn dto);
}