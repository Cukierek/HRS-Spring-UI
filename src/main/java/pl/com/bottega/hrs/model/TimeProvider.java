package pl.com.bottega.hrs.model;

import java.time.Clock;
import java.time.LocalDate;

public interface TimeProvider {

    LocalDate MAX_DATE = LocalDate.parse("9999-01-01");

    Clock clock();

    default LocalDate today() {
        return LocalDate.now(clock());
    }

}
