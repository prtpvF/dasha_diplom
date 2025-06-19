package diplom.by.robot.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import diplom.by.robot.util.ZonedDateTimeDeserializer;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Builder
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDto {

    private Integer id;
    private String name;
    private String description;
    private String pathToImage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    @JsonDeserialize(using = ZonedDateTimeDeserializer.class)
    private ZonedDateTime dateOfEvent;
    private MultipartFile image;
}
