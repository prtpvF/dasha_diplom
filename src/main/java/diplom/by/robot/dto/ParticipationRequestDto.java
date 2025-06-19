package diplom.by.robot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParticipationRequestDto {

        private Integer id;
        private UserDto author;
        private CourseDto course;

}
