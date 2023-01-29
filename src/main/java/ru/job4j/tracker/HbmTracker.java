package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;
import ru.job4j.tracker.model.ItemHB;
import ru.job4j.tracker.store.StoreHB;

import java.util.List;
import java.util.Optional;

public class HbmTracker implements StoreHB, AutoCloseable {
    private final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
            .configure().build();
    private final SessionFactory sf = new MetadataSources(registry)
            .buildMetadata().buildSessionFactory();

    @Override
    public ItemHB add(ItemHB item) {
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.save(item);
            session.getTransaction().commit();

        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return item;
    }

    @Override
    public boolean replace(int id, ItemHB item) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "UPDATE Item SET name = :fName WHERE id = :fId")
                    .setParameter("fName", "new name")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    @Override
    public boolean delete(int id) {
        boolean result = false;
        Session session = sf.openSession();
        try {
            session.beginTransaction();
            session.createQuery(
                    "DELETE Item WHERE id = :fId")
                    .setParameter("fId", id)
                    .executeUpdate();
            session.getTransaction().commit();
            result = true;
        } catch (Exception e) {
            session.getTransaction().rollback();
        }
        session.close();
        return result;
    }

    @Override
    public List<ItemHB> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List<ItemHB> result = session.createQuery("from Item").list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public List<ItemHB> findByName(String key) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item i where i.name = :name");
        query.setParameter("name", key);
        List<ItemHB> result = query.list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    @Override
    public Optional findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Query query = session.createQuery("from Item i where i.id = :id");
        query.setParameter("id", id);
        Optional result = query.list().stream().findFirst();
        session.getTransaction().commit();
        session.close();
        return result;
    }


    @Override
    public void close() {
        StandardServiceRegistryBuilder.destroy(registry);
    }
}