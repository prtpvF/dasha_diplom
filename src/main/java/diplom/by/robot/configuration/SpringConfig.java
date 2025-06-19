package diplom.by.robot.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Properties;

/**
 * This class configures useful beans like: mail sender and model mapper
 * @version 1.0
 *
 * */
@Configuration
@EnableScheduling
public class SpringConfig implements WebMvcConfigurer {

    /**
     * ModelMapper is a serializer which helps in serializing objects.
     */
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setPropertyCondition(context ->
                context.getSource() != null);
        return modelMapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");
    }

    /**
     * Bean of a mail sender. This bean connects to Google Mail sender and sends emails via it.
     * We pass some params like: <b style="color: white;">host</b> (<i><u>clarifying what mail sender exactly we want to use</u></i>),
     * <b style="color: white;">port</b>, <b style="color: white;">username</b>, and <b style="color: white;">password</b>.
     *
     */
    @Bean
    public JavaMailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);

        mailSender.setUsername("caplyginmihail48@gmail.com");
        mailSender.setPassword("zmtm bkcc rvha qltt");

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }


}
