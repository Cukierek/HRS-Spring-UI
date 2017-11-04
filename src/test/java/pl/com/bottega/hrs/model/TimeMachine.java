package pl.com.bottega.hrs.model;

import java.time.*;

public class TimeMachine implements TimeProvider {

    private Clock currentClock;

    public TimeMachine() {
        reset();
    }

    @Override
    public Clock clock() {
        return currentClock;
    }

    public void travel(Duration duration) {
        currentClock = Clock.offset(currentClock, duration);
    }

    public void travel(LocalDate destination) {
        currentClock = Clock.fixed(LocalDateTime.of(destination, LocalTime.now()).toInstant(ZoneOffset.UTC), ZoneId.systemDefault());
    }

    public void reset() {
        currentClock = Clock.systemDefaultZone();
    }
}
