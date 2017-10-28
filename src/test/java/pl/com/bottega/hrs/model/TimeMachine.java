package pl.com.bottega.hrs.model;

import java.time.Clock;
import java.time.Duration;

public class TimeMachine implements TimeProvider {

    private Clock currentClock = Clock.systemDefaultZone();

    @Override
    public Clock clock() {
        return currentClock;
    }

    public void travel(Duration duration) {
        currentClock = Clock.offset(currentClock, duration);
    }
}
