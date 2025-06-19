package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne
        @JoinColumn(name = "author_id", nullable = false)
        private UserEntity author;

        @ManyToOne
        @JoinColumn(name = "course_id")
        private CourseEntity course;

        private String text;

        @CreationTimestamp
        private LocalDateTime created;

        @UpdateTimestamp
        private LocalDateTime updated;
}
