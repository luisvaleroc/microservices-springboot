package com.alpine.auth_server.services;

import com.alpine.auth_server.dtos.TokenDto;
import com.alpine.auth_server.dtos.UserDto;

public interface AuthService {

    TokenDto login(UserDto user);
    TokenDto validateToken(TokenDto token);
}
