package diplom.by.robot.controller;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.facade.CourseFacade;
import diplom.by.robot.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with courses' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/course")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class CourseController {

        private final CourseService courseService;
        private final CourseFacade courseFacade;

        @PostMapping("/new")
        public CourseDto createCourse(@ModelAttribute CourseDto courseDto) {
            return courseService.createCourse(courseDto);
        }

        @GetMapping("/participant/{id}")
        public List<UserDto> getCourseParticipants(@PathVariable("id") Integer courseId) {
            return courseService.getAllCourseParticipant(courseId);
        }

        @GetMapping("/{id}")
        public CourseDto getCourseById(@PathVariable("id") Integer courseId,
                                       @RequestHeader(value = "Authorization", required = false) String token) {
            return courseService.getCourseDtoById(courseId, token);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteCourse(@PathVariable("id") int id) {
            return courseFacade.deleteCourse(id);
        }

        @PatchMapping("/{id}")
        public CourseDto updateCourse(@PathVariable("id") int id, @ModelAttribute CourseDto courseDto) {
            return courseService.updateCourse(courseDto, id);
        }


        @GetMapping("/short")
        public List<CourseDto> coursesShort() {
            return courseService.getShortCourses();
        }

        @GetMapping("/all")
        public List<CourseDto> getAllCourses() {
            return courseService.getAll();
        }

        @PostMapping("/import")
        public ResponseEntity<CourseDto> importCourse() {
            courseService.saveCoursesInXml();

            return new ResponseEntity<CourseDto>(new CourseDto(), HttpStatus.OK);
        }
}