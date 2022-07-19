package org.restaurant;

import lombok.NonNull;
import lombok.experimental.NonFinal;
import org.restaurant.model.Table;

import java.util.List;

/**
 * Loads available tables in the restaurant from storage.
 * For the purpose of this exercise list of tables is hardcoded.
 */
public class TablesRepository {
    @NonNull
    public List<Table> loadAll() {
        return List.of(
                new Table(1, 1),
                new Table(2, 2),
                new Table(3, 3),
                new Table(4, 4),
                new Table(5, 5),
                new Table(6, 6),
                new Table(7, 7),
                new Table(8, 8)
        );
    }
}
