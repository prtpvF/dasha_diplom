package diplom.by.robot.controller;

import diplom.by.robot.dto.LoginDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpHeaders;
import java.util.Map;

/**
 * This controller contains endpoint what connected with auth(login, registration) business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

        private final AuthService authService;

        @PostMapping("/registration")
        public UserDto registration(@RequestBody UserDto userDto) {
                return authService.registration(userDto);
        }

        @PostMapping("/login")
        public Map<String, String> login(@RequestBody LoginDto loginDto) {
                return authService.login(loginDto);
        }

        @PostMapping("/refresh")
        public Map<String, String> refresh(@RequestHeader("Authorization") String refreshToken) {
                return authService.refreshJwtTokens(refreshToken.substring(7));
        }

        @PostMapping("/logout")
        public ResponseEntity logout(@RequestHeader("Authorization") String token) {
                return authService.logout(token);
        }
}