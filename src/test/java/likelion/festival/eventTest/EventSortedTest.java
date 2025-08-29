package likelion.festival.eventTest;

import likelion.festival.domain.Event;
import likelion.festival.repository.EventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class EventSortedTest {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.of(2024, 9, 14, 12, 0);

        // --- Inactive Events ---
        createEvent("E_past", now.minusDays(1).withHour(10), now.minusDays(1).withHour(11));
        createEvent("E_future", now.plusHours(1), now.plusHours(2));

        // --- Active Events ---
        createEvent("E6_starts_now_ends_1230", now, now.withMinute(30));
        createEvent("E5_starts_now_ends_1300", now, now.plusHours(1));
        createEvent("E2_starts_1100_ends_1300", now.minusHours(1), now.plusHours(1));
        createEvent("E1_starts_1100_ends_1400", now.minusHours(1), now.plusHours(2));
        createEvent("E7_starts_1000_ends_1230", now.minusHours(2), now.withMinute(30));
        createEvent("E4_starts_1000_ends_1300", now.minusHours(2), now.plusHours(1));
        createEvent("E3_starts_0900_ends_2000", now.minusHours(3), now.plusHours(8));
        createEvent("E8_starts_0800_ends_1201", now.minusHours(4), now.withMinute(1));

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("현재 진행중인 이벤트를 시작시간 내림차순, 종료시간 오름차순으로 정렬하여 조회한다")
    void findActiveEvents_sortsCorrectly() {
        // given
        LocalDateTime now = LocalDateTime.of(2024, 9, 14, 12, 0);
        List<String> expectedOrder = List.of(
                "E6_starts_now_ends_1230",
                "E5_starts_now_ends_1300",
                "E2_starts_1100_ends_1300",
                "E1_starts_1100_ends_1400",
                "E7_starts_1000_ends_1230",
                "E4_starts_1000_ends_1300",
                "E3_starts_0900_ends_2000",
                "E8_starts_0800_ends_1201"
        );

        // when
        List<Event> activeEvents = eventRepository.findActiveEvents(now);

        // then
        System.out.println("\n--- Sorted Active Events ---");
        activeEvents.forEach(event ->
                System.out.printf("Title: %-30s | Start: %s | End: %s%n",
                        event.getTitle(),
                        event.getStartTime(),
                        event.getEndTime())
        );
        System.out.println("------------------------------------------------------------------------------------\n");

        List<String> actualTitles = activeEvents.stream()
                .map(Event::getTitle)
                .collect(Collectors.toList());

        assertThat(activeEvents).hasSize(8);
        assertThat(actualTitles).isEqualTo(expectedOrder);
    }

    private void createEvent(String title, LocalDateTime startTime, LocalDateTime endTime) {
        Event event = Event.builder()
                .title(title)
                .startTime(startTime)
                .endTime(endTime)
                .notice(null)
                .build();
        entityManager.persist(event);
    }
}
