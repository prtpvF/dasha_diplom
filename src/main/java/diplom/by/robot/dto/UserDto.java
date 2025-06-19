package diplom.by.robot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class UserDto {

        private Integer id;
        private String username;
        private String firstName;
        private String password;
        private String lastName;
        private String email;
        private String pathToImage;
        private String phone;
        private String role;
        private MultipartFile image;
        private Boolean isActive;
        private Integer age;
        private List<ParticipationRequestDto> requests = new ArrayList<>();
        private List<CourseDto> courses = new ArrayList<>();
}