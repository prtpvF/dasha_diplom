package diplom.by.robot.service;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.exceptions.NonUniqueEntityException;
import diplom.by.robot.exceptions.UnauthorizedException;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.UserRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

        private final UserRepository userRepository;
        private final JwtUtil jwtUtil;
        private final ModelMapper modelMapper;
        private final ConverterUtil converterUtil;
        private final PasswordEncoder passwordEncoder;
        private final ImageService imageService;

        public UserEntity findUserByUsername(String username) {
            Optional<UserEntity> user = userRepository.findByUsername(username);

            if (user.isPresent()) {
                return user.get();
            }
            throw new EntityNotFoundException("юзер с таким никнеймом не найден!");
        }

        public UserEntity getUserByToken(String token) {
            try {
                return findUserByUsername(jwtUtil.validateTokenAndRetrieveClaim(token.substring(7)));
            } catch (Exception e) {
                throw new UnauthorizedException("Пожалуйста, авторизуйтесь еще раз");
            }
        }

        public UserEntity findUserById(Integer id) {
            Optional<UserEntity> user = userRepository.findById(id);
            if (user.isPresent()) {
                return user.get();
            }
            throw new EntityNotFoundException("cannot find user with this id!");
        }

    public UserDto updateUserInfo(UserDto userDto, String token) {
        UserEntity currentUser = getUserByToken(token);
        processUpdate(userDto, currentUser);
        modelMapper.map(userDto, currentUser);

        if(userDto.getPassword() != null) {
            currentUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }

        if(userDto.getImage() != null ) {
            if(currentUser.getPathToImage() != null) {
                imageService.deleteImage(currentUser.getPathToImage());
            }
            currentUser.setPathToImage(imageService.saveImage(userDto.getImage()));
        }

        UserEntity updatedTutor = userRepository.save(currentUser);
        return converterUtil.convertUserToUserDto(updatedTutor);
    }

    public UserDto getUserInfoByToken(String token) {
        UserEntity currentUser = getUserByToken(token);
        return converterUtil.convertUserToUserDto(currentUser);
    }

    public void processUpdate(UserDto userDto, UserEntity user) {

        if (userDto.getEmail() != null && !userDto.getEmail().equals(user.getEmail())) {
            userRepository.findByEmail(userDto.getEmail()).ifPresent(
                    ex ->
                    {
                        throw new NonUniqueEntityException(
                                "пользователь с такими данными уже существует");
                    }
            );
        }

        if (userDto.getPhone() != null && !userDto.getPhone().equals(user.getPhone())) {
            userRepository.findByPhone(userDto.getPhone()).ifPresent(
                    ex -> {
                        throw new NonUniqueEntityException(
                                "пользователь с такими данными уже существует");
                    }
            );
        }

        if (userDto.getUsername() != null && !userDto.getUsername().equals(user.getUsername())) {
            userRepository.findByUsername(userDto.getUsername()).ifPresent(
                    ex -> {
                        throw new NonUniqueEntityException(
                                "пользователь с такими данными уже существует");
                    }
            );
        }

    }
}
