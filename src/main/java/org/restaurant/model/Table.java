package org.restaurant.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NavigableMap;
import java.util.TreeMap;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Table {
    /*Reservation time period in hours */
    public static final int RESERVATION_PERIOD = 2;
    private final int id;
    private int size;
    private final NavigableMap<LocalDateTime, Reservation> reservations = new TreeMap();

    private final BiPredicate<LocalDateTime, LocalDateTime> isBeforePredicate = (current, future) -> {
        LocalDateTime currentEndReservation = current.plusHours(RESERVATION_PERIOD);
        return currentEndReservation.isBefore(future) || currentEndReservation.isEqual(future);
    };

    private final BiPredicate<LocalDateTime, LocalDateTime> isAfterPredicate = (current, previous) -> {
        LocalDateTime previousEndReservation = previous.plusHours(RESERVATION_PERIOD);
        return previousEndReservation.isBefore(current) || previousEndReservation.isEqual(current);
    };

    public boolean isFree(@NonNull LocalDateTime reservationAt) {
        if (reservations.containsKey(reservationAt)) {
            return false;
        }
        LocalDateTime localDateTimeFuture = reservations.higherKey(reservationAt);
        LocalDateTime localDateTimePast = reservations.lowerKey(reservationAt);

        if (localDateTimeFuture == null && localDateTimePast == null) {
            return true;
        } else if (localDateTimeFuture == null) {
            return isAfterPredicate.test(reservationAt, localDateTimePast);
        } else if (localDateTimePast == null) {
            return isBeforePredicate.test(reservationAt, localDateTimeFuture);
        } else {
            return isBeforePredicate.test(reservationAt, localDateTimeFuture) && isAfterPredicate.test(reservationAt, localDateTimePast);
        }
    }

    public void reserveTable(@NonNull Reservation reservation) {
        reservations.put(reservation.getReservationDateTime(), reservation);
    }

    @NonNull
    public List<Reservation> reservationsAt(@NonNull LocalDate date) {
        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = start.plusHours(24);

        return reservations.subMap(start, true, end, false).values().stream().collect(Collectors.toUnmodifiableList());
    }
}
