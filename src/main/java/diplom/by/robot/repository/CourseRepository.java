package diplom.by.robot.repository;

import diplom.by.robot.model.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Integer> {

    Optional<CourseEntity> findById(int id);
}
