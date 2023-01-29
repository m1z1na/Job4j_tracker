package ru.job4j.tracker;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.ItemHB;

import java.util.List;

public class HibernateRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            var item = new ItemHB();
            item.setName("Learn Hibernate");
            create(item, sf);
            System.out.println(item);
            item.setName("Learn Hibernate 5.");
            update(item, sf);
            System.out.println(item);
            ItemHB rsl = findById(item.getId(), sf);
            System.out.println(rsl);
            delete(rsl.getId(), sf);
            List<ItemHB> list = findAll(sf);
            for (ItemHB it : list) {
                System.out.println(it);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static ItemHB create(ItemHB item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public static void update(ItemHB item, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.update(item);
        session.getTransaction().commit();
        session.close();
    }

    public static void delete(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        ItemHB item = new ItemHB();
        item.setId(id);
        session.delete(item);
        session.getTransaction().commit();
        session.close();
    }

    public static List<ItemHB> findAll(SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        List<ItemHB> result = session.createQuery("from ru.job4j.tracker.model.ItemHB", ItemHB.class).list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    public static ItemHB findById(Integer id, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        ItemHB result = session.get(ItemHB.class, id);
        session.getTransaction().commit();
        session.close();
        return result;
    }
}