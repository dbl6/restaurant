package org.restaurant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * Serves as simple singletons factory.
 */
public class RestaurantBeans {
    private static final TablesRepository tablesRepository = new TablesRepository();
    private static final ReservationService reservationService = new ReservationService(tablesRepository);
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public static ReservationService reservationService() {
        return reservationService;
    }

    public static ObjectMapper objectMapper() {
        return mapper;
    }
}
