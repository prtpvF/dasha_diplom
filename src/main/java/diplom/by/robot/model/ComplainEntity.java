package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "complain")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ComplainEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String text;

        @CreationTimestamp
        private LocalDateTime createdAt;

        @ManyToOne
        @JoinColumn(name = "author_id")
        private UserEntity author;

        @ManyToOne
        @JoinColumn(name = "course_id")
        private CourseEntity course;
}
