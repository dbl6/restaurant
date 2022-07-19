package org.restaurant;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.restaurant.model.BookRequest;
import org.restaurant.model.Reservation;
import org.restaurant.model.Table;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ReservationServiceTest {

    private final LocalDateTime reservationTime = LocalDateTime.parse("2022-07-19T12:00:00");
    @Mock
    private TablesRepository tablesRepository;

    @Test
    public void shouldReserveTable(){
        //given
        when(tablesRepository.loadAll()).thenReturn(List.of(new Table(1, 4)));
        ReservationService reservationService = new ReservationService(tablesRepository);
        BookRequest bookRequest = new BookRequest("Darek", reservationTime, 4);

        //when
        boolean isSuccess = reservationService.reserveTable(bookRequest);

        //then
        assertTrue(isSuccess);
    }

    @Test
    public void shouldNotReserveTooSmallTable(){
        when(tablesRepository.loadAll()).thenReturn(List.of(new Table(1, 3)));
        ReservationService reservationService = new ReservationService(tablesRepository);
        BookRequest bookRequest = new BookRequest("Darek", reservationTime, 4);

        //when
        boolean isSuccess = reservationService.reserveTable(bookRequest);

        //then
        assertFalse(isSuccess);
    }

    @Test
    public void shouldNotReserveBookedTable(){
        Table table = new Table(1, 4);
        table.reserveTable(new Reservation("Darek", reservationTime.minusHours(1), 4));
        when(tablesRepository.loadAll()).thenReturn(List.of(table));
        ReservationService reservationService = new ReservationService(tablesRepository);
        BookRequest bookRequest = new BookRequest("Darek", reservationTime, 4);

        //when
        boolean isSuccess = reservationService.reserveTable(bookRequest);

        //then
        assertFalse(isSuccess);
    }

    @Test
    public void shouldReturnAllReservations(){
        Table table1 = new Table(1, 4);
        table1.reserveTable(new Reservation("Darek", reservationTime, 3));
        Table table2 = new Table(1, 4);
        table2.reserveTable(new Reservation("Darek", reservationTime, 4));

        when(tablesRepository.loadAll()).thenReturn(List.of(table1, table2));
        ReservationService reservationService = new ReservationService(tablesRepository);

        //when
        List<Reservation> reservations = reservationService.reservationsAt(reservationTime.toLocalDate());

        //then
        assertEquals(2, reservations.size());
    }
}
