package diplom.by.robot.controller;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.PersonDetailsService;
import diplom.by.robot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PersonController {

    private final UserService userService;

    @GetMapping("/profile")
    public UserDto getUserInfo(@RequestHeader("Authorization") String token) {
        return userService.getUserInfoByToken(token);
    }

    @PatchMapping("/profile")
    public UserDto updateTutor(@ModelAttribute UserDto userDto,
                               @RequestHeader("Authorization") String token) {
        return userService.updateUserInfo(userDto, token);
    }
}
