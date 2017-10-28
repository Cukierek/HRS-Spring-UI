package pl.com.bottega.hrs.model;

import java.time.Clock;

public class StandardTimeProvider implements TimeProvider {

    @Override
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
