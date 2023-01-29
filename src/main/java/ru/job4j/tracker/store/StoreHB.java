package ru.job4j.tracker.store;

import ru.job4j.tracker.model.ItemHB;

import java.util.List;
import java.util.Optional;

public interface StoreHB extends AutoCloseable {
    ItemHB add(ItemHB item);
    boolean replace(int id, ItemHB item);
    boolean delete(int id);
    List<ItemHB> findAll();
    List<ItemHB> findByName(String name);
    Optional findById(int id);
}
