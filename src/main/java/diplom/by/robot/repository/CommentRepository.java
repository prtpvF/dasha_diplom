package diplom.by.robot.repository;

import diplom.by.robot.model.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {

    @Query("SELECT c FROM CommentEntity c WHERE c.course.id = :courseId")
    List<CommentEntity> findAllByCourse(@Param("courseId") Integer courseId);
}
