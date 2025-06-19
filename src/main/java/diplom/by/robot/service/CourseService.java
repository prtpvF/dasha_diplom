package diplom.by.robot.service;

import diplom.by.robot.dto.CourseDto;
import diplom.by.robot.dto.UserDto;
import diplom.by.robot.jwt.JwtUtil;
import diplom.by.robot.model.CourseEntity;
import diplom.by.robot.model.UserEntity;
import diplom.by.robot.repository.CourseRepository;
import diplom.by.robot.util.ConverterUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService {

        private final CourseRepository courseRepository;
        private final ConverterUtil converterUtil;
        private final JwtUtil jwtUtil;
        private final ImageService imageService;
        private final UserService userService;
        private final ModelMapper modelMapper;

        @Transactional
        public CourseDto createCourse(CourseDto courseDto) {
                processCourseCreating(courseDto);
                CourseEntity course = converterUtil.convertCourseForCreating(courseDto);
                UserEntity tutor = userService.findUserByUsername(courseDto.getTutorUsername());
                course.setTutor(
                        tutor
                        );
                course.setEndDate(courseDto.getEnd().toLocalDateTime());
                course.setStartDate(courseDto.getStart().toLocalDateTime());
                course.setPathToImg(imageService.saveImage(courseDto.getImage()));
                CourseEntity createdCourse = courseRepository.save(course);
                log.info("created course {}", course);
                return converterUtil.convertCourseToDto(createdCourse);
        }

        public void saveCoursesInXml() {
                List<CourseDto> courses = courseRepository.findAll()
                        .stream()
                        .map(converterUtil::convertCourseToDto)
                        .toList();


                try (Workbook workbook = new XSSFWorkbook()) {
                        Sheet sheet = workbook.createSheet("Courses");
                        CellStyle headerStyle = workbook.createCellStyle();
                        Font headerFont = workbook.createFont();
                        headerFont.setBold(true);
                        headerStyle.setFont(headerFont);
                        Row headerRow = sheet.createRow(0);
                        String[] columns = {"Название", "Начало", "Конец", "Описание", "Преподаватель", "Расписание"};
                        for (int i = 0; i < columns.length; i++) {
                                Cell cell = headerRow.createCell(i);
                                cell.setCellValue(columns[i]);
                                cell.setCellStyle(headerStyle);
                                sheet.autoSizeColumn(i);
                        }

                        int rowNum = 1;
                        for (CourseDto course : courses) {
                                Row row = sheet.createRow(rowNum++);
                                row.createCell(0).setCellValue(course.getName());
                                row.createCell(1).setCellValue(course.getStartDate());
                                row.createCell(2).setCellValue(course.getEndDate());
                                row.createCell(3).setCellValue(course.getDescription());
                                row.createCell(4).setCellValue(course.getTutorUsername());
                                row.createCell(5).setCellValue(course.getSchedule());
                        }

                        for (int i = 0; i < columns.length; i++) {
                                sheet.autoSizeColumn(i);
                        }

                        try (FileOutputStream fileOut = new FileOutputStream("excel\\courses.xlsx")) {
                                workbook.write(fileOut);
                        }

                        System.out.println("Excel файл успешно создан!");
                } catch (IOException e) {
                        e.printStackTrace();
                }

        }

        public CourseDto updateCourse(CourseDto courseDto, Integer courseId) {
                CourseEntity course = getCourseEntityById(courseId);
                modelMapper.map(courseDto, course);

                if(courseDto.getImage() != null) {

                        if(course.getPathToImg() != null) {
                                imageService.deleteImage(course.getPathToImg());
                        }

                        course.setPathToImg(imageService.saveImage(courseDto.getImage()));
                }

                CourseEntity updatedCourse = courseRepository.save(course);
                return converterUtil.convertCourseToDto(updatedCourse);
        }

        public List<UserDto> getAllCourseParticipant(Integer courseId) {
                CourseEntity course = getCourseEntityById(courseId);
                return course.getStudents().stream()
                        .map(converterUtil::convertCourseParticipantToUserDto)
                        .toList();
        }

        public List<CourseDto> getAll() {
               List<CourseEntity> courseEntities = courseRepository.findAll();
               List<CourseDto> dtoList = new ArrayList<>();

               for (CourseEntity courseEntity : courseEntities) {
                       dtoList.add(converterUtil.convertCourseToDto(courseEntity));
               }
               return dtoList;
        }

        public List<CourseDto> getShortCourses() {
                List<CourseEntity> courseEntities = courseRepository.findAll();
                List<CourseDto> dtoList = new ArrayList<>();

                for (CourseEntity courseEntity : courseEntities) {
                        dtoList.add(new CourseDto(courseEntity.getId(), courseEntity.getName()));
                }
                return dtoList;
        }


        @Transactional
        public CourseDto getCourseDtoById(Integer id, String token) {
                CourseEntity course = courseRepository.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));

                CourseDto courseDto = converterUtil.convertCourseToDto(course);

                if (token != null && !token.isBlank()) {
                        UserEntity user = userService.getUserByToken(token);
                        courseDto.setIsRecorded(user != null && isUserInCourseRequests(course, user));
                }

                return courseDto;
        }

        private boolean isUserInCourseRequests(CourseEntity course, UserEntity user) {
                if (course.getRequests() == null || course.getRequests().isEmpty()) {
                        return false;
                }

                return course.getRequests().stream()
                        .anyMatch(requestUser -> requestUser.getId() == (user.getId()));
        }

        public CourseEntity getCourseEntityById(Integer id) {
               return courseRepository.findById(id)
                       .orElseThrow(() -> new EntityNotFoundException("Курс не найден"));
        }

        @Transactional
        public ResponseEntity deleteCourse(int id) {
                Optional<CourseEntity> course = courseRepository.findById(id);

                if (course.isPresent()) {
                        courseRepository.delete(course.get());
                        return new ResponseEntity(OK);
                }
                else {
                        throw new EntityNotFoundException("cannot find course with id " + id);
                }

        }

        private void processCourseCreating(CourseDto courseDto) {
                LocalDateTime startDate = courseDto.getStart().toLocalDateTime();
                LocalDateTime endDate = courseDto.getEnd().toLocalDateTime();

                if (endDate.isBefore(startDate) ||
                        startDate.equals(endDate) ||
                        startDate.isBefore(LocalDateTime.now())) {
                        throw new IllegalArgumentException("вы передали неверное время!");
                }
        }
}
