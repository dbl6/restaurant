package org.restaurant;

import lombok.NonNull;
import org.restaurant.model.BookRequest;
import org.restaurant.model.Reservation;
import org.restaurant.model.Table;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service that handles restaurant requests.
 */
public class ReservationService {
    @NonNull
    private final TablesRepository tablesRepository;
    @NonNull
    private List<Table> tables;

    ReservationService(@NonNull TablesRepository tablesRepository) {
        this.tablesRepository = tablesRepository;
        this.tables = tablesRepository.loadAll();
    }

    /**
     * Used to make reservation in the restaurant.
     *
     * @param bookRequest represent reservation request.
     * @return true if reservation succeeded or false
     * if there are other reservations at the same time.
     */
    public boolean reserveTable(@NonNull BookRequest bookRequest) {
        for (Table table : tables) {
            if (table.getSize() >= bookRequest.getTableSize()) {
                synchronized (tables) {
                    if (table.isFree(bookRequest.getReservationAt())) {
                        table.reserveTable(new Reservation(bookRequest.getClientName(), bookRequest.getReservationAt(), bookRequest.getTableSize()));
                        return true;
                    }
                }
            }
        }

        return false;
    }

    /**
     * Collects all reservations at given day.
     *
     * @param date day of reservations.
     * @return list of reservations at given day.
     */
    @NonNull
    public List<Reservation> reservationsAt(@NonNull LocalDate date) {
        return tables.stream().flatMap(table -> table.reservationsAt(date).stream()).collect(Collectors.toUnmodifiableList());
    }
}
