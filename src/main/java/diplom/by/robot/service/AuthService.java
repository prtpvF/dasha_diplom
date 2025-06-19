package diplom.by.robot.service;

import diplom.by.robot.dto.LoginDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.exceptions.NonUniqueEntityException;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static diplom.by.robot.util.RoleEnum.USER;
import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class AuthService {

        private final UserRepository userRepository;
        private final PasswordEncoder passwordEncoder;
        private final JwtUtil jwtUtil;


        public UserDto registration(UserDto userDto) {
                checkAvailabilityForRegistration(userDto);
                userRepository.save(convertDtoToUser(userDto));
                return userDto;
        }

        public Map<String, String> login(LoginDto loginDto) {
                Optional<UserEntity> user = userRepository.findByUsername(loginDto.getUsername());
                if ( user.isPresent()) {
                boolean passwordMatch = passwordEncoder.matches(loginDto.getPassword(), user.get().getPassword());
                        if(passwordMatch) {
                                Map<String, String> data = new HashMap<>();
                                data.put("access-token",
                                        jwtUtil.generateAccessToken(user.get().getUsername(), user.get().getRole()));
                                data.put("refresh-token",
                                        jwtUtil.generateRefreshToken(user.get().getUsername()));
                                return data;
                        }
                }
               throw new EntityNotFoundException("Произошла ошибка при авторизации. Проверьте данные");
        }

        public UserEntity convertDtoToUser(UserDto userDto) {
                UserEntity userEntity = new UserEntity();
                userEntity.setAge(userDto.getAge());
                userEntity.setEmail(userDto.getEmail());
                userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
                userEntity.setFirstName(userDto.getFirstName());
                userEntity.setLastName(userDto.getLastName());
                userEntity.setRole(USER.name());
                userEntity.setIsActive(true);
                userEntity.setPhone(userDto.getPhone());
                userEntity.setUsername(userDto.getUsername());
                return userEntity;
        }

        public Map<String, String> refreshJwtTokens(String refreshToken) {
                String role = getRoleByToken(refreshToken);
                return jwtUtil.refreshTokens(refreshToken, role);
        }

        public ResponseEntity logout(String token) {
        jwtUtil.invalidToken(token);
        return new ResponseEntity(OK);
        }

        public void checkAvailabilityForRegistration(UserDto userDto) {
                Optional<UserEntity> user = userRepository.findByUsernameOrPhoneOrEmail(
                        userDto.getUsername(),
                        userDto.getPhone(),
                        userDto.getEmail());
                if(user.isPresent()) {
                        throw new NonUniqueEntityException("пользователь с одним из таких полей уже существует");
                }
        }

        private String getRoleByToken(String token) {
                String username = jwtUtil.validateTokenAndRetrieveClaim(token);
                UserEntity user = userRepository.findByUsername(username).orElseThrow(
                        () -> new EntityNotFoundException("cannot find person")
                );
                return user.getRole();
        }
}