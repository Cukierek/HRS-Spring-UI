package pl.com.bottega.hrs.infrastructure;

import org.springframework.stereotype.Component;
import pl.com.bottega.hrs.model.TimeProvider;

import java.time.Clock;

@Component
public class StandardTimeProvider implements TimeProvider {

    @Override
    public Clock clock() {
        return Clock.systemDefaultZone();
    }
}
