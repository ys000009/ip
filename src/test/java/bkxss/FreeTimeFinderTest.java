package bkxss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

/** Tests the core free-time search logic. */
class FreeTimeFinderTest {
    private static final LocalDateTime SEARCH_START = LocalDateTime.of(2026, 9, 12, 9, 0);
    private static final LocalDateTime SEARCH_END = LocalDateTime.of(2026, 9, 12, 18, 0);

    @Test
    void findEarliestStart_noEvents_returnsSearchStart() {
        LocalDateTime result = FreeTimeFinder.findEarliestStart(
                SEARCH_START, SEARCH_END, Duration.ofHours(4), List.of()).orElseThrow();

        assertEquals(SEARCH_START, result);
    }

    @Test
    void findEarliestStart_unsortedOverlappingEvents_returnsStartAfterMergedEvents() {
        Event laterEvent = new Event("workshop", "2026-09-12 1100", "2026-09-12 1500");
        Event earlierEvent = new Event("lecture", "2026-09-12 0800", "2026-09-12 1200");

        LocalDateTime result = FreeTimeFinder.findEarliestStart(SEARCH_START, SEARCH_END,
                Duration.ofHours(3), List.of(laterEvent, earlierEvent)).orElseThrow();

        assertEquals(LocalDateTime.of(2026, 9, 12, 15, 0), result);
    }

    @Test
    void findEarliestStart_gapExactlyMatchesDuration_returnsGapStart() {
        Event firstEvent = new Event("lecture", "2026-09-12 0900", "2026-09-12 1000");
        Event secondEvent = new Event("workshop", "2026-09-12 1200", "2026-09-12 1800");

        LocalDateTime result = FreeTimeFinder.findEarliestStart(SEARCH_START, SEARCH_END,
                Duration.ofHours(2), List.of(firstEvent, secondEvent)).orElseThrow();

        assertEquals(LocalDateTime.of(2026, 9, 12, 10, 0), result);
    }

    @Test
    void findEarliestStart_noLargeEnoughGap_returnsEmpty() {
        Event morningEvent = new Event("lecture", "2026-09-12 0900", "2026-09-12 1200");
        Event afternoonEvent = new Event("workshop", "2026-09-12 1300", "2026-09-12 1800");

        assertTrue(FreeTimeFinder.findEarliestStart(SEARCH_START, SEARCH_END,
                Duration.ofHours(2), List.of(morningEvent, afternoonEvent)).isEmpty());
    }

    @Test
    void findEarliestStart_legacyEvent_throwsException() {
        Event legacyEvent = new Event("meeting", "Mon 2pm", "4pm");

        assertThrows(IllegalArgumentException.class, () -> FreeTimeFinder.findEarliestStart(
                SEARCH_START, SEARCH_END, Duration.ofHours(2), List.of(legacyEvent)));
    }
}
