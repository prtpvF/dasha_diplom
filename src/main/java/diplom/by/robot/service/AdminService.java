package diplom.by.robot.service;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.exceptions.IllegalEntityException;
import diplom.by.robot.exceptions.NonUniqueEntityException;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.UserRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static diplom.by.robot.util.RoleEnum.TUTOR;
import static org.springframework.http.HttpStatus.OK;


/**
 * It is a service what contains admin functionality.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

        private final UserRepository userRepository;
        private final ConverterUtil converterUtil;
        private final ImageService imageService;
        private final UserService userService;
        private final AuthService authService;
        private final JwtUtil jwtUtil;
        private final ModelMapper modelMapper;
        private final PasswordEncoder passwordEncoder;
        private final CourseService courseService;

        @Transactional
        public ResponseEntity<?> changePersonStatus(String username,
                                                 String token) {
            String adminUsername = jwtUtil.validateTokenAndRetrieveClaim(token.substring(7));
            if(adminUsername.equals(username)) {
                throw new IllegalEntityException("вы не можете забанить самого себя!");
            }
            UserEntity user = userService.findUserByUsername(username);
            user.setIsActive(!user.getIsActive());
            userRepository.save(user);
            return new ResponseEntity<>(OK);
        }

        public UserDto updateTutor(UserDto userDto, int id) {
            UserEntity tutor = userService.findUserById(id);
            processUpdate(userDto, tutor);
            if(userDto.getImage() != null) {
                tutor.setPathToImage(imageService.saveImage(userDto.getImage()));
            }
            modelMapper.map(userDto, tutor);
            tutor.setId(id);
            if(userDto.getPassword() != null) {
                tutor.setPassword(passwordEncoder.encode(userDto.getPassword()));
            }
            UserEntity updatedTutor = userRepository.save(tutor);
            return converterUtil.convertUserToUserDto(updatedTutor);
        }

        @Transactional
        public UserDto createTutor(UserDto userDto) {
           authService.checkAvailabilityForRegistration(userDto);
           UserEntity tutor = authService.convertDtoToUser(userDto);
           tutor.setRole(TUTOR.name());
            if(userDto.getImage() != null) {
                tutor.setPathToImage(imageService.saveImage(userDto.getImage()));
            }
           UserEntity createdTutor = userRepository.save(tutor);
           return converterUtil.convertUserToUserDto(createdTutor);
        }

        public ResponseEntity<?> deleteTutor(Integer id) {
            UserEntity tutor = userService.findUserById(id);
            imageService.deleteImage(tutor.getPathToImage());
            for (CourseEntity course : tutor.getCourses()) {
                courseService.deleteCourse(course.getId());
            }
            userRepository.delete(tutor);
            return new ResponseEntity<>(OK);
        }

        @Transactional
        public List<UserDto> getTutors() {
            List<UserEntity> users = userRepository.findAllTutors();
            return users.stream()
                    .map(converterUtil::convertUserToUserDto)
                    .collect(Collectors.toList());
        }

    @Transactional
    public List<UserDto> getTutorsShort() {
        List<UserEntity> users = userRepository.findAllTutors();
        return users.stream()
                .map(converterUtil::convertUserToUserDtoShort)
                .collect(Collectors.toList());
    }

        public void processUpdate(UserDto userDto, UserEntity tutor) {

            if (userDto.getEmail() != null && !userDto.getEmail().equals(tutor.getEmail())) {
                 userRepository.findByEmail(userDto.getEmail()).ifPresent(
                         ex ->
                         {
                             throw new NonUniqueEntityException(
                                     "пользователь с такими данными уже существует");
                         }
                 );
            }

            if (userDto.getPhone() != null && !userDto.getPhone().equals(tutor.getPhone())) {
                userRepository.findByPhone(userDto.getPhone()).ifPresent(
                        ex -> {
                            throw new NonUniqueEntityException(
                                    "пользователь с такими данными уже существует");
                        }
                );
            }

            if (userDto.getUsername() != null && !userDto.getUsername().equals(tutor.getUsername())) {
                userRepository.findByUsername(userDto.getUsername()).ifPresent(
                        ex -> {
                            throw new NonUniqueEntityException(
                                    "пользователь с такими данными уже существует");
                        }
                );
            }

        }
}