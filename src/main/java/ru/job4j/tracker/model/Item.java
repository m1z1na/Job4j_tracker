package ru.job4j.tracker.model;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Objects;

@Data
public class Item {

    private int id;
    private String name;
    private LocalDateTime created;

    public Item(String name) {
        this.name = name;
    }

    public Item(int id, String name, LocalDateTime created) {
        this.id = id;
        this.name = name;
        this.created = created;
    }

    public Item(int id, String name, String created) {
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, created: %s", id, name, created);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id && name.equals(item.name) && created.equals(item.created);
    }
}