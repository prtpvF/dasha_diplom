package diplom.by.robot.facade;

import diplom.by.robot.dto.CommentDto;
import diplom.by.robot.repository.CommentRepository;
import diplom.by.robot.service.CommentService;
import diplom.by.robot.service.CourseService;
import diplom.by.robot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentFacade {

        private final CommentService commentService;
        private final CourseService courseService;
        private final UserService userService;

        public CommentDto createComment(CommentDto commentDto,
                                        String token,
                                        Integer courseId) {
            return commentService.createComment(commentDto,
                    userService.getUserByToken(token),
                    courseService.getCourseEntityById(courseId));
        }

        public ResponseEntity<?> deleteComment(Integer commentId, String token) {
            return commentService.deleteComment(commentId, userService.getUserByToken(token));
        }
}
