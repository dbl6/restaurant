package org.restaurant;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.restaurant.model.Reservation;

import java.time.LocalDateTime;

public class MapperTest {
    private final ObjectMapper mapper = RestaurantBeans.objectMapper();
    @Test
    public void shouldSerialize() throws JsonProcessingException {
        Reservation reservation = new Reservation("Darek", LocalDateTime.now(), 4);
        String s = mapper.writeValueAsString(reservation);
        System.out.println(s);
    }
}
