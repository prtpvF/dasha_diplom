package diplom.by.robot.controller;

import diplom.by.robot.dto.ParticipationRequestDto;
import diplom.by.robot.facade.ParticipationFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with participation requests' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/participation")
@CrossOrigin(origins = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.OPTIONS})
public class ParticipationController {

    private final ParticipationFacade participationFacade;;

    @PostMapping("/{id}")
    public ResponseEntity<?> createParticipation(@PathVariable("id") Integer courseId,
                                                 @RequestHeader("Authorization") String token) {
        return participationFacade.createParticipationRequest(token, courseId);
    }

    @GetMapping("/byTutor")
    public List<ParticipationRequestDto> getTutorsRequests(@RequestHeader("Authorization") String token) {
        return participationFacade.getAllByTutor(token);
    }

    @PostMapping("/answer/{id}")
    public ResponseEntity<?> acceptRequest(@PathVariable("id") Integer requestId,
                                           @RequestHeader("Authorization") String token,
                                           @RequestParam("result") Boolean result) {
        return participationFacade.answerRequest(requestId, token, result);
    }
}
