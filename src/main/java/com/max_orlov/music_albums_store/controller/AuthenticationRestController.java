package com.max_orlov.music_albums_store.controller;

import com.max_orlov.music_albums_store.dto.AuthenticationRequestDto;
import com.max_orlov.music_albums_store.dto.EntityMapper;
import com.max_orlov.music_albums_store.dto.NewUserDto;
import com.max_orlov.music_albums_store.model.User;
import com.max_orlov.music_albums_store.security.JwtTokenUtil;
import com.max_orlov.music_albums_store.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthenticationRestController {
    private static final Logger logger = LogManager.getLogger();

    private static final String THREAD_CONTEXT_KEY = "userName";
    private static final String JWT_HEADER_NAME = "jwtToken";
    private static final String USER_NOT_FOUND_MESSAGE = "User '%s' doesn't exists";
    private static final String AUTHENTICATION_EXCEPTION_MESSAGE = "Invalid userName/password combination";
    private static final String LOGIN_MESSAGE = "User '{}' logged in";
    private static final String LOGOUT_MESSAGE = "User '{}' logged out";
    private static final String NEW_USER_MESSAGE = "New user '{}' successfully added";

    private AuthenticationManager authenticationManager;
    private UserService userService;
    private JwtTokenUtil jwtTokenUtil;
    private EntityMapper mapper;

    @PostMapping(value = "/login")
    public ResponseEntity<?> authenticate(AuthenticationRequestDto request, HttpServletResponse response) {
        try {
            String userName = request.userName();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, request.password()));
            User user = userService.findByUsername(userName)
                    .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, userName)));
            ThreadContext.put(THREAD_CONTEXT_KEY, userName);
            logger.info(LOGIN_MESSAGE, userName);
            String token = jwtTokenUtil.createToken(userName, user.getRole().name());
            response.setHeader(JWT_HEADER_NAME, token);
            return new ResponseEntity<>(mapper.userToUserDto(user), HttpStatus.OK);
        } catch (AuthenticationException e) {
            logger.error(e.getMessage(), e);
            return new ResponseEntity<>(AUTHENTICATION_EXCEPTION_MESSAGE, HttpStatus.FORBIDDEN);
        }
    }

    @PostMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler securityContextLogoutHandler = new SecurityContextLogoutHandler();
        String userName = Arrays.stream(request.getCookies())
                .filter(cookie -> THREAD_CONTEXT_KEY.equals(cookie.getName()))
                .toList().getFirst().getValue();
        logger.info(LOGOUT_MESSAGE, userName);
        securityContextLogoutHandler.logout(request, response, null);
    }

    @PostMapping("/newUser")
    public ResponseEntity<?> newUser(NewUserDto newUserDto) {
      try {
        User user = userService.saveNewUser(newUserDto.userName(), newUserDto.password());
        logger.info(NEW_USER_MESSAGE, newUserDto.userName());
        return new ResponseEntity<>(mapper.userToUserDto(user), HttpStatus.OK);
      } catch (Exception e) {
        logger.error(e.getMessage(), e);
          return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }

}
