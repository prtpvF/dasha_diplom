package diplom.by.robot.exceptions.handler;

import com.auth0.jwt.exceptions.JWTVerificationException;
import diplom.by.robot.exceptions.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class ErrHandler {

        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<Object> personAlreadyExistsExceptionHandler(DataIntegrityViolationException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(EntityNotFoundException.class)
        public ResponseEntity<?> cannotFindPersonExceptionHandler(EntityNotFoundException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), NOT_FOUND);
        }

        @ExceptionHandler(ConstraintViolationException.class)
        public ResponseEntity<Object> handleValidationExceptions(ConstraintViolationException e) {
            log.error(e.getMessage());
            Set<ConstraintViolation<?>> messages = e.getConstraintViolations();
            List<String> errorMessages = new ArrayList<>();
            for (ConstraintViolation<?> violation : messages) {
               errorMessages.add(violation.getMessage());
            }
            return new ResponseEntity<>(errorMessages, BAD_REQUEST);
        }

        @ExceptionHandler(JWTVerificationException.class)
        public ResponseEntity<Object> tokenHasExpiredHandler(JWTVerificationException e){
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }

        @ExceptionHandler(IllegalEntityException.class)
        public ResponseEntity<Object> illegalEntityExceptionHandler(IllegalEntityException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(NonUniqueEntityException.class)
        public ResponseEntity<Object> nonUniqueEntityExceptionHandler(NonUniqueEntityException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Object> illegalArgumentExceptionHandler(IllegalArgumentException e){
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(FileExtensionException.class)
        public ResponseEntity<Object> fileExtensionExceptionHandler(FileExtensionException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(FileDeletingException.class)
        public ResponseEntity<Object> fileDeletingExceptionHandler(FileDeletingException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(CannotDeleteCommentException.class)
        public ResponseEntity<Object> cannotDeleteCommentExceptionHandler(CannotDeleteCommentException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(CannotSendParticipationRequestException.class)
        public ResponseEntity<Object> cannotSendParticipationRequestExceptionHandler(CannotSendParticipationRequestException e){
            log.warn(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), BAD_REQUEST);
        }

        @ExceptionHandler(UnauthorizedException.class)
        public ResponseEntity<Object> unauthorizedExceptionHandler(UnauthorizedException e){
            log.info(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), UNAUTHORIZED);
        }
}