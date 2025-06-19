package diplom.by.robot.controller;

import diplom.by.robot.dto.EventDto;
import diplom.by.robot.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This controller contains endpoint what connected with events' business logic
 * Make with REST architecture
 * */
@RestController
@RequiredArgsConstructor
@RequestMapping("/event")
public class EventController {

        private final EventService eventService;

        @PostMapping("/new")
        public EventDto createEvent(@ModelAttribute EventDto eventDto) {
            return eventService.createEvent(eventDto);
        }

        @GetMapping("/all")
        public List<EventDto> getAllEvents() {
            return eventService.getAllEvents();
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<?> deleteEvent(@PathVariable("id") Integer eventId) {
            return eventService.deleteEvent(eventId);
        }

        @GetMapping("/{id}")
        public EventDto getEventById(@PathVariable("id") Integer eventId) {
            return eventService.getEvent(eventId);
        }

        @PatchMapping("/{id}")
        public EventDto updateEvent(@PathVariable("id") Integer id,
                                    @ModelAttribute EventDto eventDto) {
            return eventService.updateEvent(id, eventDto);
        }
}
