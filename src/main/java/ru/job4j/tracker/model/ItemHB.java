package ru.job4j.tracker.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "items")
@Data
public class ItemHB {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private LocalDateTime created = LocalDateTime.now();
    public ItemHB() {
    }

    public ItemHB(String name) {
        this.name = name;
    }

    public static ItemHB idStub(int id) {
        ItemHB item = new ItemHB();
        item.setId(id);
        return item;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("%d %s", id, name);
    }
}