package diplom.by.robot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import diplom.by.robot.util.ZonedDateTimeDeserializer;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CourseDto {

        private Integer id;
        private String name;
        private String description;
        @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
        private ZonedDateTime start;
        @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
        private ZonedDateTime end;
        private String startDate;
        private String endDate;
        private String pathToImage;
        private Boolean isRecorded;
        private MultipartFile image;
        private String tutorUsername;
        private String schedule;
        private List<UserDto> users = new ArrayList<>();

        public CourseDto() {

        }

        public CourseDto(Integer id, String name) {
                this.id = id;
                this.name = name;
        }
}