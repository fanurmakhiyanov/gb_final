package ru.fanur.service;

import ru.fanur.dto.request.LoginPasswordDtoIn;
import ru.fanur.dto.response.LoginPasswordDtoOut;
import ru.fanur.exception.AuthenticationException;
import ru.fanur.exception.JwtFilterAuthException;

public interface AuthService {
    LoginPasswordDtoOut authenticate(LoginPasswordDtoIn loginPasswordDtoIn) throws AuthenticationException;
    void logout();
}