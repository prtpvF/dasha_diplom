package diplom.by.robot.facade;

import diplom.by.robot.dto.UserDto;
import diplom.by.robot.service.CourseService;
import diplom.by.robot.service.NotificationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseFacade {

        private final CourseService courseService;
        private final NotificationService notificationService;

        public ResponseEntity<?> deleteCourse(Integer id) {

            List<UserDto> students = getAllCourseStudents(id);
            try {
                for (UserDto user : students) {
                    notificationService.sendEmail(user.getEmail(),
                            "Курс отменен",
                            "Курс отменен");
                }
            }
            catch (Exception e) {
                log.error(e.getMessage());
            }
            courseService.deleteCourse(id);
            return ResponseEntity.ok("Курс успешно отменен и уведомления отправлены.");
    }

        private List<UserDto> getAllCourseStudents(Integer id) {
            return courseService.getCourseDtoById(id, null).getUsers();
        }


}
