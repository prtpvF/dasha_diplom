package diplom.by.robot.model;

import jakarta.persistence.*;

@Entity
@Table(name = "article")
public class ArticleEntity {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;
}
