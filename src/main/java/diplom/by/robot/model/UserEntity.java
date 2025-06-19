package diplom.by.robot.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "person")
@Getter
@Setter
@AllArgsConstructor
@DynamicUpdate
@NoArgsConstructor
public class UserEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Length(min = 3, max = 20, message = "никнейм должен быть не короче 3 и не длинее 20 символов")
        private String username;
        @Length(min = 3, max = 255, message = "пароль должен быть не короче 3 и не длинее 20 символов")
        private String password;
        @Length(min = 3, max = 20, message = "имя должено быть не короче 3 и не длинее 20 символов")
        private String firstName;
        @Length(min = 3, max = 20, message = "фамилия должена быть не короче 3 и не длинее 20 символов")
        private String lastName;
        @Email(message = "почта должна быть почта формата example@example.com")
        private String email;
        @NotBlank
        @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Неверный формат телефона")
        private String phone;
        private String role;
        private String pathToImage;
        private Boolean isActive;
        @Min(value = 10, message = "минимальный возраст - 10")
        @Max(value = 120, message = "максимальный возраст - 120")
        private Integer age;

        @OneToMany(mappedBy = "author")
        private List<CommentEntity> commentEntities = new ArrayList<>();

        @OneToMany(mappedBy = "tutor")
        private List<CourseEntity> courses = new ArrayList<>();

        @OneToMany(mappedBy = "author")
        private List<ComplainEntity> complains = new ArrayList<>();

        @ManyToMany
        private List<CourseEntity> studentCourses = new ArrayList<>();

        @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
        private List<ParticipationRequestEntity> participationRequests = new ArrayList<>();

        public void addCourse(CourseEntity course) {
                courses.add(course);
        }
}
