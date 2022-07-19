package org.restaurant.model;

import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Represents reservation in the system.
 */
@Value
public class Reservation {
    @NonNull
    String customerName;
    @NonNull
    LocalDateTime reservationDateTime;
    @NonNull
    int tableSize;
}
