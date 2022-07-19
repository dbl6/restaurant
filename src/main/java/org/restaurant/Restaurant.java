package org.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muserver.*;
import org.restaurant.model.BookRequest;
import org.restaurant.model.Reservation;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Naive restaurant booking application.
 * It allows to book table and list all reservation for provided date.
 */
public class Restaurant {
    private ObjectMapper objectMapper = RestaurantBeans.objectMapper();
    private ReservationService reservationService = RestaurantBeans.reservationService();

    public static void main(String[] args) {
        new Restaurant().start();
    }

    public void start() {
        MuServer server = MuServerBuilder.httpServer()
                .addHandler(Method.GET, "/reservations/{reservationsAt}", allReservationsHandler)
                .addHandler(Method.POST, "/reservations", createReservationHandler)
                .start();

        System.out.println(server.httpUri());
    }

    public RouteHandler allReservationsHandler = (request, response, pathParams) -> {
        response.contentType(ContentTypes.APPLICATION_JSON);
        try {
            List<Reservation> reservationsAt = reservationService.reservationsAt(LocalDate.parse(pathParams.get("reservationsAt")));
            String reservations = objectMapper.writeValueAsString(reservationsAt);
            response.write(reservations);
        } catch (DateTimeParseException | JsonProcessingException e) {
            response.status(400);
        } catch (Exception ex) {
            response.status(500);
        }
    };

    public RouteHandler createReservationHandler = (request, response, pathParams) -> {
        try {
            BookRequest bookRequest = objectMapper.readValue(request.readBodyAsString(), BookRequest.class);
            boolean isReserved = reservationService.reserveTable(bookRequest);
            if (isReserved) {
                response.status(201);
            } else {
                response.status(409);
            }
        } catch (JsonProcessingException e) {
            response.status(400);
        } catch (Exception ex) {
            response.status(500);
        }
    };
}
