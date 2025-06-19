package diplom.by.robot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "token_black_list")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlackListToken {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String token;
}
