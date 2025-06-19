package diplom.by.robot.util;

import diplom.by.robot.dto.*;
import diplom.by.robot.model.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class ConverterUtil {

        private final ModelMapper modelMapper;

        public UserDto convertUserToUserDto(UserEntity user) {
            return UserDto.builder()
                    .id(user.getId())
                    .phone(user.getPhone())
                    .age(user.getAge())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .pathToImage(user.getPathToImage())
                    .role(user.getRole())
                    .email(user.getEmail())
                    .courses(user.getCourses()
                            .stream().map(this::convertSmallCourseToCourseDto).toList())
                    .build();
        }

        public UserDto convertUserToUserDtoShort(UserEntity user) {
            return UserDto.builder()
                    .id(user.getId())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .build();
        }

        public CommentDto convertCommentToCommentDto(CommentEntity comment) {
            return CommentDto.builder()
                    .id(comment.getId())
                    .courseId(comment.getCourse().getId())
                    .comment(comment.getText())
                    .author(comment.getAuthor().getUsername())
                    .build();
        }

        public EventDto convertEventToEventDto(EventEntity event) {
            return EventDto.builder()
                    .id(event.getId())
                    .name(event.getName())
                    .dateOfEvent(event.getDateOfEvent().atZone(ZoneId.of("Europe/Minsk")))
                    .description(event.getDescription())
                    .pathToImage(event.getPathToImage())
                    .build();
        }

        public EventEntity convertEventDtoToEventEntity(EventDto eventDto) {
           return  modelMapper.map(eventDto, EventEntity.class);
        }

        public UserDto convertCourseParticipantToUserDto(UserEntity user) {
        return UserDto.builder()
                .phone(user.getPhone())
                .age(user.getAge())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .username(user.getUsername())
                .pathToImage(user.getPathToImage())
                .role(user.getRole())
                .email(user.getEmail())
                .build();
    }

        public UserDto convertSmallUserToUserDto(UserEntity user) {
            return UserDto.builder()
                    .username(user.getUsername())
                    .phone(user.getPhone())
                    .email(user.getEmail())
                    .build();
        }

        public CourseDto convertSmallCourseToCourseDto(CourseEntity course) {
            CourseDto courseDto = new CourseDto();
            courseDto.setId(course.getId());
            courseDto.setName(course.getName());
            courseDto.setPathToImage(course.getPathToImg());
            courseDto.setTutorUsername(course.getTutor().getUsername());
        return courseDto;
        }

        public CourseDto convertCourseToDto(CourseEntity course) {
            CourseDto courseDto = new CourseDto();
                courseDto.setId(course.getId());
                courseDto.setName(course.getName());
                courseDto.setPathToImage(course.getPathToImg());
                courseDto.setStartDate(course.getStartDate().toString());
                courseDto.setEndDate(course.getEndDate().toString());
                courseDto.setSchedule(course.getSchedule());
                courseDto.setDescription(course.getDescription());
                courseDto.setTutorUsername(course.getTutor().getUsername());
                return courseDto;
        }

        public CourseEntity convertCourseForCreating(CourseDto courseDto) {
            return modelMapper.map(courseDto, CourseEntity.class);
        }

        public ParticipationRequestDto participationRequestToDto(ParticipationRequestEntity request) {
            return ParticipationRequestDto.builder()
                    .id(request.getId())
                    .author(convertSmallUserToUserDto(request.getAuthor()))
                    .course(convertSmallCourseToCourseDto(request.getCourse()))
                    .build();
        }
}