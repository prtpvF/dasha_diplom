package diplom.by.robot.service;

import diplom.by.robot.dto.CommentDto;
import diplom.by.robot.exceptions.CannotDeleteCommentException;
import diplom.by.robot.model.CommentEntity;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CommentRepository;
import diplom.by.robot.util.ConverterUtil;
import diplom.by.robot.util.RoleEnum;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

        private final CommentRepository commentRepository;
        private final ConverterUtil converterUtil;

        public CommentDto createComment(CommentDto commentDto,
                                        UserEntity author,
                                        CourseEntity course) {
            CommentEntity comment = new CommentEntity();
            comment.setAuthor(author);
            comment.setCourse(course);
            comment.setText(comment.getText());
            return converterUtil.convertCommentToCommentDto(commentRepository.save(comment));
        }

        public CommentDto getCommentDtoById(Integer commentId) {
            return converterUtil.convertCommentToCommentDto(
                    getCommentEntityById(commentId)
            );
        }

        public List<CommentDto> getAllCourseComments(Integer courseId) {
            return commentRepository.findAllByCourse(courseId)
                    .stream().map(converterUtil::convertCommentToCommentDto)
                    .toList();
        }

        public ResponseEntity<?> deleteComment(Integer commentId, UserEntity user) {
            CommentEntity commentEntity = getCommentEntityById(commentId);

            if (isUserAuthorOfCommentOrAdmin(commentEntity, user)) {
                commentRepository.delete(commentEntity);
                return ResponseEntity.ok().build();
            }
            else {
                throw new CannotDeleteCommentException(
                        "Вы не можете удалить этот коментарий"
                );
            }
        }

        private CommentEntity getCommentEntityById(Integer commentId) {
            return commentRepository.findById(commentId)
                    .orElseThrow(
                            () -> new EntityNotFoundException(
                                    "Ошибка поиска коментария"
                            )
                    );
        }

        private boolean isUserAuthorOfCommentOrAdmin(CommentEntity commentEntity, UserEntity user) {
            return commentEntity.getAuthor().getUsername().equals(user.getUsername())
                    || user.getRole().equals(RoleEnum.ADMIN.name());
        }
}
