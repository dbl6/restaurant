package org.restaurant.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * Represents request for reservation.
 */
@Value
public class BookRequest {
    @NonNull
    String clientName;
    @NonNull
    LocalDateTime reservationAt;
    int tableSize;

    public BookRequest(
            @JsonProperty("clientName") String clientName,
            @JsonProperty("reservationAt") LocalDateTime reservationAt,
            @JsonProperty("tableSize") int tableSize) {
        this.clientName = clientName;
        this.reservationAt = reservationAt;
        this.tableSize = tableSize;
    }
}
