package diplom.by.robot.controller;

import diplom.by.robot.dto.FeedbackDto;
import diplom.by.robot.service.FeedbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping("/feedback")
    public ResponseEntity<?> feedback(@RequestBody FeedbackDto feedbackDto) {
        feedbackService.feedback(feedbackDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
