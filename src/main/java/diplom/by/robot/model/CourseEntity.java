package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Getter
@Setter
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class CourseEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
        @Length(min=3, max=150, message = "имя должно быть от 3 до 30 символов в длину")
        private String name;
        @Length(min = 10, max = 1000, message = "описание не может бытькороче 10 или длинее 1000 символов")
        private String description;
        private LocalDateTime startDate;
        private LocalDateTime endDate;
        private String schedule;

        @ManyToOne
        @JoinColumn(name = "tutor_id", nullable = false)
        private UserEntity tutor;
        private String pathToImg;

        @ManyToMany
        @JoinTable(
                name = "student_course",
                joinColumns = @JoinColumn(name = "person_id"),
                inverseJoinColumns = @JoinColumn(name = "course_id"))
        private List<UserEntity> students = new ArrayList<>();

        @OneToMany(mappedBy = "course", cascade = {CascadeType.REMOVE}, orphanRemoval = true)
        private List<CommentEntity> comments = new ArrayList<>();

        @OneToMany(mappedBy = "course")
        private List<ComplainEntity> complains = new ArrayList<>();

        @OneToMany(mappedBy = "course", cascade = CascadeType.REMOVE)
        private List<ParticipationRequestEntity> requests = new ArrayList<>();


        public void addStudent(UserEntity student) {
                students.add(student);
        }
}
