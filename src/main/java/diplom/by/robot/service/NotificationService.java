package diplom.by.robot.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

        private final JavaMailSender javaMailSender;

        public void sendEmail(String toEmail,
                              String subject,
                              String body){
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("caplyginmihail48@gmail.com");
            message.setTo(toEmail);
            message.setText(body);
            message.setSubject(subject);
            try {
                javaMailSender.send(message);
                log.info("mail send successfully...");
            } catch (Exception e) {
                log.error(String.format("Не удалось отправить письмо по адресу: %s", toEmail));
            }
        }

        public void sendMessageWithAttachments(
                                        String toEmail,
                                        String subject,
                                        String body,
                                        File file) throws MessagingException {
            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);
            helper.addAttachment("course.xlsx",file);
            try {
                javaMailSender.send(message);
                log.info("mail send successfully...");
            } catch (Exception e) {
                log.error(String.format("Не удалось отправить письмо по адресу: %s", toEmail));
            }
        }
}