package com.alpine.auth_server.services;


import com.alpine.auth_server.dtos.TokenDto;
import com.alpine.auth_server.dtos.UserDto;
import com.alpine.auth_server.entities.UserEntity;
import com.alpine.auth_server.helpers.JwtHelper;
import com.alpine.auth_server.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Transactional
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtHelper jwtHelper;

    private static final String USER_EXCEPTION_MSG = "Error to auth user";

    @Override
    public TokenDto login(UserDto user) {
        final var userFromDB= this.userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG));
        this.validPassword(user, userFromDB);

        return TokenDto.builder()
                .accessToken(this.jwtHelper.createToken(userFromDB.getUsername()))
                .build();
    }

    @Override
    public TokenDto validateToken(TokenDto token) {

        if(this.jwtHelper.validateToken(token.getAccessToken())){
            return TokenDto.builder()
                    .accessToken(token.getAccessToken())
                    .build();
        }


        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
    }


    private void validPassword(UserDto userDto, UserEntity userEntity) {
        if(!this.passwordEncoder.matches(userDto.getPassword(), userEntity.getPassword())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, USER_EXCEPTION_MSG);
        }
    }
}
