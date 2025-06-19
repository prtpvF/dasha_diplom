package diplom.by.robot.cron;

import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * It is a scheduler service.
 * How does it work? It cals a method what delete all ended courses every 24 hours
 * @version 1.0
* */
@Service
@RequiredArgsConstructor
@Slf4j
public class SchedulerService {

        private final CourseRepository courseRepository;
        private final EventRepository eventRepository;

        /**
         * Delete all ended courses and events every 24 hours
         * */
        @Scheduled(fixedRate = 86400000)
        private void deleteEndedCourses() {
            log.info("start deleting ended courses");
            LocalDateTime now = LocalDateTime.now();
            deleteEndedCourses(now);
            deleteEndedEvents(now);
        }

        /**
         * Method iterates collection and removes ended courses
         * @param now current date and time
         */
        private void deleteEndedCourses(LocalDateTime now) {
            courseRepository.findAll().forEach(course -> {
                if (course.getEndDate().isBefore(now)) {
                    courseRepository.delete(course);
                    log.info("Deleted ended course: {}", course.getName());
                }
            });
        }

        /**
         * Method iterates collection and removes ended courses
         * @param now current date and time
         */
        private void deleteEndedEvents(LocalDateTime now) {
            eventRepository.findAll().forEach(event -> {
                if (event.getDateOfEvent().isBefore(now)) {
                    eventRepository.delete(event);
                    log.info("Deleted ended event: {}", event.getName());
                }
            });
        }
}