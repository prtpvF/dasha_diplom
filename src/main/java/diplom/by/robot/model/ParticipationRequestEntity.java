package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.catalina.User;

@Entity
@Table(name = "participation_request")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @ManyToOne(cascade = CascadeType.REFRESH)
        @JoinColumn(name = "author_id")
        private UserEntity author;

        @ManyToOne
        @JoinColumn(name = "course_id")
        private CourseEntity course;

        private String status;

}