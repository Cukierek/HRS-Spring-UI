package pl.com.bottega.hrs.model;

import java.time.Clock;
import java.time.LocalDate;

public interface TimeProvider {

    Clock clock();

    default LocalDate today() {
        return LocalDate.now(clock());
    }

}
