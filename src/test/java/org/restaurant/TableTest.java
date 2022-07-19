package org.restaurant;

import org.junit.Test;
import org.restaurant.model.Reservation;
import org.restaurant.model.Table;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;


public class TableTest {

    private final LocalDateTime reservationTime = LocalDateTime.parse("2022-07-19T12:00:00");

    @Test
    public void shouldFindFreeTimeSlotIfNoReservations() {
        //given
        Table table = new Table(1, 10);
        //when
        boolean free = table.isFree(reservationTime);
        //then
        assertTrue(free);
    }

    @Test
    public void shouldNotFindFreeForEqualTimestamps() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime, 1));

        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertFalse(free);
    }

    @Test
    public void shouldNotFindFreeForOverlapping() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime.minusHours(1), 1));
        table.reserveTable(new Reservation("Lola", reservationTime.plusHours(1), 1));

        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertFalse(free);
    }

    @Test
    public void shouldNotFindFreeForOverlappingLeft() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime.minusHours(1), 1));

        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertFalse(free);
    }

    @Test
    public void shouldNotFindFreeForOverlappingRight() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime.plusHours(1), 1));

        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertFalse(free);
    }

    @Test
    public void shouldFindFreeInBetweenInclusive() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime.minusHours(2), 1));
        table.reserveTable(new Reservation("Lola", reservationTime.plusHours(2), 1));
        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertTrue(free);
    }

    @Test
    public void shouldFindFreeInBetweenExclusive() {
        //given
        Table table = new Table(1, 10);
        table.reserveTable(new Reservation("Lola", reservationTime.minusHours(3), 1));
        table.reserveTable(new Reservation("Lola", reservationTime.plusHours(3), 1));
        //when
        boolean free = table.isFree(reservationTime);

        //then
        assertTrue(free);
    }

    @Test
    public void shouldReturnReservationsAtGivenDay() {
        //given
        Table table = new Table(1, 1);
        table.reserveTable(new Reservation("", reservationTime, 1));
        table.reserveTable(new Reservation("", reservationTime.plusHours(24), 1));

        //when
        List<Reservation> reservations = table.reservationsAt(reservationTime.toLocalDate());
        //then
        assertEquals(1, reservations.size());
    }

    @Test
    public void shouldReserveTable() {
        //given
        Table table = new Table(1, 1);
        //when
        table.reserveTable(new Reservation("Darek", reservationTime, 1));
        //then
        List<Reservation> reservations = table.reservationsAt(reservationTime.toLocalDate());
        assertEquals(1, reservations.size());
    }
}
