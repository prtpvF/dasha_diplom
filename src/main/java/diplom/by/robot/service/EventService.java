package diplom.by.robot.service;

import diplom.by.robot.dto.EventDto;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.EventEntity;
import diplom.by.robot.repository.EventRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EventService {

        private final EventRepository eventRepository;
        private final JwtUtil jwtUtil;
        private final ConverterUtil converterUtil;
        private final ImageService imageService;
        private final ModelMapper modelMapper;

        public EventDto createEvent(EventDto eventDto) {
            EventEntity eventEntity = converterUtil.convertEventDtoToEventEntity(eventDto);
            validateDateOfEventField(eventDto.getDateOfEvent());
            eventEntity.setPathToImage(imageService.saveImage(eventDto.getImage()));
            eventEntity.setDateOfEvent(LocalDateTime.from(eventDto.getDateOfEvent()));
            return converterUtil.convertEventToEventDto(eventRepository.save(eventEntity));
        }

        public List<EventDto> getAllEvents() {
            return eventRepository.findAll().stream().map(
                    converterUtil::convertEventToEventDto).toList();
        }

        public EventDto getEvent(Integer id) {
            return converterUtil.convertEventToEventDto(getEventEntityById(id));
        }

        public ResponseEntity<?> deleteEvent(Integer eventId) {
            EventEntity eventEntity = getEventEntityById(eventId);

            if(eventEntity.getPathToImage() != null) {
                imageService.deleteImage(eventEntity.getPathToImage());
            }

            eventRepository.delete(eventEntity);
            return new ResponseEntity<>("Мероприятие удалено", HttpStatus.OK);
        }

        public EventDto updateEvent(Integer id, EventDto eventDto) {
            EventEntity eventEntity = getEventEntityById(id);
            mapFieldsFromDtoToEventEntity(eventDto, eventEntity);

            if(eventDto.getImage() != null) {

                if(eventEntity.getPathToImage() != null) {
                    imageService.deleteImage(eventEntity.getPathToImage());
                }

                eventEntity.setPathToImage(
                        imageService.saveImage(eventDto.getImage())
                );
            }

            eventEntity = eventRepository.save(eventEntity);
            return converterUtil.convertEventToEventDto(eventEntity);
        }

        private void validateDateOfEventField(ZonedDateTime dateOfEvent) {
            if(dateOfEvent.isBefore(ZonedDateTime.now())) {
                throw new IllegalArgumentException(
                        "Время мероприятия не должно быть меньше текущего!");
            }
        }

    private void mapFieldsFromDtoToEventEntity(EventDto eventDto, EventEntity eventEntity) {
            if (eventDto.getDateOfEvent() != null) {
                validateDateOfEventField(eventDto.getDateOfEvent());
                eventEntity.setDateOfEvent(LocalDateTime.from(eventDto.getDateOfEvent()));
            }
        modelMapper.map(eventDto, eventEntity);
    }

        private EventEntity getEventEntityById(Integer eventId) {
            return eventRepository.findById(eventId)
                    .orElseThrow(() -> new EntityNotFoundException(
                            "ошибка поиска ивента"
                    ));
        }
}