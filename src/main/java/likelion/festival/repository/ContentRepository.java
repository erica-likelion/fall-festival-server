package likelion.festival.repository;

import likelion.festival.domain.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ContentRepository extends JpaRepository<Content, Long> {

    @Query("SELECT c FROM Content c WHERE FUNCTION('DATE', :now) " +
            "BETWEEN FUNCTION('DATE', c.startTime) AND FUNCTION('DATE', c.endTime) AND FUNCTION('TIME', :now) " +
            "BETWEEN FUNCTION('TIME', c.startTime) AND FUNCTION('TIME', c.endTime) " +
            "ORDER BY c.startTime DESC, c.endTime ASC")
    List<Content> findActiveContents(@Param("now") LocalDateTime now);

}