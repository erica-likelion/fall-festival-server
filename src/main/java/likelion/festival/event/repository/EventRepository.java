package likelion.festival.event.repository;

import likelion.festival.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE :now BETWEEN e.startTime AND e.endTime ORDER BY e.startTime DESC, e.endTime ASC")
    List<Event> findActiveEvents(@Param("now") LocalDateTime now);

}