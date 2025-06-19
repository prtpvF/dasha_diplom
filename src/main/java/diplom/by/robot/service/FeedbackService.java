package diplom.by.robot.service;

import diplom.by.robot.dto.FeedbackDto;
import diplom.by.robot.model.CourseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final NotificationService notificationService;
    private final CourseService courseService;

    public void feedback(FeedbackDto dto) {
        CourseEntity course = courseService.getCourseEntityById(Integer.parseInt(dto.getProgram()));
        notificationService.sendEmail(
                "dashkasmina@gmail.com",
                "Заявка",
                String.format("Пользователь %s оставил запрос на курс: %s. Свяжитесь с ним по телефону: %s",
                        dto.getName(), course.getName(), dto.getPhone())
        );
    }
}
