package diplom.by.robot.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with admins' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class AdminController {

        private final AdminService adminService;

        @GetMapping("/tutors")
        public List<UserDto> tutors() {
            return adminService.getTutors();
        }

        @GetMapping("/tutors/short")
        public List<UserDto> tutorsShort() {
            return adminService.getTutorsShort();
        }

        @PostMapping("/person-status")
        public ResponseEntity changePersonStatus(@RequestBody ObjectNode json,
                                                 @RequestHeader("Authorization") String token) {
             adminService.changePersonStatus(json.get("username").asText(), token);
             return new ResponseEntity(HttpStatus.OK);
        }

        @PostMapping("/tutor")
        public UserDto newTutor(@ModelAttribute UserDto userDto) {
            return adminService.createTutor(userDto);
        }

        @PatchMapping("/tutor/{id}")
        public UserDto updateTutor(@RequestBody UserDto userDto,
                                @PathVariable("id") Integer id) {
            return adminService.updateTutor(userDto, id);
        }

        @DeleteMapping("/tutors/{id}")
        public ResponseEntity deleteTutor(@PathVariable("id") Integer id) {
            return adminService.deleteTutor(id);
        }
}