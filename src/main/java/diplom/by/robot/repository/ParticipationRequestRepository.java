package diplom.by.robot.repository;

import diplom.by.robot.model.ParticipationRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequestEntity, Integer> {

        @Query(value = "SELECT pr FROM ParticipationRequestEntity pr " +
                "WHERE pr.author.username = :username " +
                "AND pr.course.id = :id")
        Optional<ParticipationRequestEntity> findByAuthorUsernameAndCourseId(@Param("username") String authorUsername,
                                                                             @Param("id") Integer courseId);

}
