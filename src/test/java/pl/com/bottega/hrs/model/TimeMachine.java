package pl.com.bottega.hrs.model;

import java.time.Clock;

public class TimeMachine implements TimeProvider {

    private Clock currentClock = Clock.systemDefaultZone();

    @Override
    public Clock clock() {
        return currentClock;
    }
}
