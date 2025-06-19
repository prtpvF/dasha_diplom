package diplom.by.robot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    private Integer id;
    private String comment;
    private String author;
    private Integer courseId;
}
