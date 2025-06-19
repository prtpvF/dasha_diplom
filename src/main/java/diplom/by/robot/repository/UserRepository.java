package diplom.by.robot.repository;

import diplom.by.robot.model.UserEntity;
import org.hibernate.sql.ast.tree.expression.JdbcParameter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    @Query("SELECT u FROM UserEntity u WHERE " +
            "(u.username = :username AND :username IS NOT NULL AND LENGTH(:username) > 0) OR " +
            "(u.phone = :phone AND :phone IS NOT NULL AND LENGTH(:phone) > 0) OR " +
            "(u.email = :email AND :email IS NOT NULL AND LENGTH(:email) > 0)")
    Optional<UserEntity> findByUsernameOrPhoneOrEmail(@Param("username") String username,
                                                      @Param("phone") String phone,
                                                      @Param("email") String email);

    Optional<UserEntity> findByUsername(String username);

    @Query(value = "SELECT u FROM UserEntity u WHERE u.role = 'TUTOR'")
    List<UserEntity> findAllTutors();

    Optional<UserEntity> findByEmail(String email);

    Optional<UserEntity> findByPhone(String phone);
}
