package diplom.by.robot.controller;

import diplom.by.robot.dto.CommentDto;
import diplom.by.robot.facade.CommentFacade;
import diplom.by.robot.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with comments'(CRUD operations) business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

     private final CommentService commentService;
     private final CommentFacade commentFacade;

     @PostMapping("/new/{id}")
     public CommentDto createComment(@RequestBody CommentDto commentDto,
                                     @PathVariable("id") Integer courseId,
                                     @RequestHeader("Authorization") String token) {
         return commentFacade.createComment(commentDto, token, courseId);
     }

     @GetMapping("/{id}")
     public CommentDto getCommentById(@PathVariable("id") Integer commentId) {
         return commentService.getCommentDtoById(commentId);
     }

     @GetMapping("/course/{id}")
     public List<CommentDto> getCourseComments(@PathVariable("{id}") Integer courseId) {
         return commentService.getAllCourseComments(courseId);
     }

     @DeleteMapping("/{id}")
     public ResponseEntity<?> deleteComment(@PathVariable("id") Integer commentId,
                                            @RequestHeader("Authorization") String token) {
         return commentFacade.deleteComment(commentId, token);

     }
}
