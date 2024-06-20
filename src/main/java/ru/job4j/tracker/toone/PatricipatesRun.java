package ru.job4j.tracker.toone;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import ru.job4j.tracker.model.ItemHB;


import java.util.List;

public class PatricipatesRun {
    public static void main(String[] args) {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        try {
            SessionFactory sf = new MetadataSources(registry).buildMetadata().buildSessionFactory();
            var role = new Role();
            role.setName("ADMIN");
            create(role, sf);
            var user = new User();
            user.setName("Admin Admin");
            user.setMessengers(List.of(
                    new UserMessenger(0, "tg", "@tg"),
                    new UserMessenger(0, "wu", "@wu")
            ));
            user.setRole(role);
            create(user, sf);
            var item = new ItemHB();
            item.setName("Learn Hibernate");
            item.setParticipates(List.of(user));
            create(item, sf);
            sf.openSession()
                    .createQuery("from ItemHB where id = :fId", ItemHB.class)
                    .setParameter("fId", item.getId())
                    .getSingleResult()
                    .getParticipates()
                    .forEach(System.out::println);
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    public static <T> T create(T model, SessionFactory sf) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.persist(model);
        session.getTransaction().commit();
        session.close();
        return model;
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
        List result = session.createQuery("from ru.job4j.tracker.Item").list();
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