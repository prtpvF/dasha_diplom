package diplom.by.robot;


import diplom.by.robot.service.NotificationService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@SpringBootApplication
public class RobotApplication {

	@Autowired
	private NotificationService emailSenderServices;

	public static void main(String[] args) {
		SpringApplication.run(RobotApplication.class, args);
	}

}
