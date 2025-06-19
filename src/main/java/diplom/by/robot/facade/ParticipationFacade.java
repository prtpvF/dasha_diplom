package diplom.by.robot.facade;

import diplom.by.robot.dto.ParticipationRequestDto;
import diplom.by.robot.exceptions.CannotSendParticipationRequestException;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.ParticipationRequestEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.service.CourseService;
import diplom.by.robot.service.NotificationService;
import diplom.by.robot.service.ParticipationService;
import diplom.by.robot.service.UserService;
import jakarta.mail.SendFailedException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.angus.mail.smtp.SMTPAddressFailedException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/*
* ParticipationFacade is a class what helps to solve architecture problem like side effect
*
* Patter: Facade
* */
@Service
@RequiredArgsConstructor
@Slf4j
public class ParticipationFacade {

    private final ParticipationService participationService;
    private final UserService userService;
    private final CourseService courseService;
    private final NotificationService notificationService;

    public ResponseEntity<?> createParticipationRequest(String token, Integer courseId) {
        UserEntity user = userService.getUserByToken(token);
        CourseEntity course = courseService.getCourseEntityById(courseId);
         participationService.sendRequestToParticipate(user,
                course);
         notificationService.sendEmail(course.getTutor().getEmail(),
                 "New request",
                 "You have a new participation request. Check your account");
         log.info("New participation request sent");
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    public List<ParticipationRequestDto> getAllByTutor(String token) {
        UserEntity user = userService.getUserByToken(token);
        return participationService.getAllTutorsParticipationRequests(user);
    }

    public ResponseEntity<?> answerRequest(Integer requestId, String token, Boolean result) {
        UserEntity tutor = userService.getUserByToken(token);
        ParticipationRequestEntity request = participationService.getRequestEntityById(requestId);
        if(request.getStatus().equals("IN_PROCESS")) {
            if (result) {
                acceptRequest(request, tutor);
            } else {
                rejectRequest(request, tutor);
            }
        } else {
            throw new CannotSendParticipationRequestException(
                    "Вы не можете дважды оптравлять запрос на участие");
        }

            return new ResponseEntity<>("Ответ принят", HttpStatus.OK);
    }

    private void acceptRequest(ParticipationRequestEntity request, UserEntity tutor) {
        participationService.acceptRequest(request.getId(), tutor);
        notificationService.sendEmail(
                request.getAuthor().getEmail(),
                "Запрос принят",
                "Мы рады сообщить что вы зачисленны в число студентов"
        );
    }

    private void rejectRequest(ParticipationRequestEntity request, UserEntity tutor) {
        participationService.declineRequest(request.getId(), tutor);
        notificationService.sendEmail(
                request.getAuthor().getEmail(),
                "Отказ",
                "Уважаеммый пользователь. К сожалению вы не были зачислены в список студентов"
        );
    }
}
