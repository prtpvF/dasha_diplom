package diplom.by.robot.service;

import diplom.by.robot.exceptions.FileDeletingException;
import diplom.by.robot.exceptions.FileExtensionException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageService {

        @Value("${save_path}")
        private String UPLOAD_DIR;

        private final String[] IMAGE_EXTENSIONS = new String[]{"jpg", "jpeg", "png"};

        public String saveImage(MultipartFile file) {

            log.info("start saving image");
            Path uploadPath = Path.of(UPLOAD_DIR).toAbsolutePath().normalize();
            if (!Files.exists(uploadPath)) {
                try {
                    Files.createDirectories(uploadPath);
                } catch (IOException e) {
                    throw new FileExtensionException("Не удалось создать директорию для загрузки");
                }
            }
            String extension = FilenameUtils.getExtension(file.getOriginalFilename());
            String baseName = FilenameUtils.getBaseName(file.getOriginalFilename())
                    .replaceAll(" ","_");

            if (Arrays.stream(IMAGE_EXTENSIONS).noneMatch(extension::contains)) {
                    throw new FileExtensionException("неверный формат файла, должен быть только: jpg, jpeg или png");
            }

            String uniqueFilename = createUniqueFilename(baseName, extension);
            Path targetPath = uploadPath.resolve(uniqueFilename);
            try {
                file.transferTo(targetPath);
            } catch (IOException e) {
                log.error(e.getMessage());
                throw new FileExtensionException("ошибка загрузки файла");
            }

            return "uploads/"+uniqueFilename;
        }

        public void deleteImage(String pathToFile) {
            try {
                File file = new File(pathToFile);
                    file.delete();
            } catch (Exception e) {
                throw new FileDeletingException("Ошибка удаления фотографии");
            }
        }

        private  String createUniqueFilename(String baseName, String extension) {

                String timestamp = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
                return baseName + "_" + timestamp + "." + extension;
        }
}
