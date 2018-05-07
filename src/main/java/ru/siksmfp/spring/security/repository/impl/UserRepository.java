package ru.siksmfp.spring.security.repository.impl;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import ru.siksmfp.spring.security.entity.Role;
import ru.siksmfp.spring.security.entity.UserEntity;
import ru.siksmfp.spring.security.exception.DAOException;
import ru.siksmfp.spring.security.repository.api.GenericRepository;

import javax.annotation.PostConstruct;
import javax.persistence.NoResultException;
import java.util.Arrays;
import java.util.List;

/**
 * @author Artem Karnov @date 4/17/2018.
 * @email artem.karnov@t-systems.com
 */
@Repository
public class UserRepository extends GenericRepository<UserEntity, Long> {

    @PostConstruct
    public void setUp() {
        deleteAll();

        UserEntity user1 = new UserEntity("email1", "name1", "secondName1", "1", Role.ADMIN);
        UserEntity user2 = new UserEntity("email2", "name2", "secondName2", "2", Role.USER);
        UserEntity user3 = new UserEntity("email3", "name3", "secondName3", "3", Role.USER);
        UserEntity user4 = new UserEntity("email4", "name4", "secondName4", "4", Role.ROOT);
        UserEntity user5 = new UserEntity("email5", "name5", "secondName5", "5", Role.USER);
        UserEntity user6 = new UserEntity("email6", "name6", "secondName6", "6", Role.USER);
        UserEntity user7 = new UserEntity("email7", "name7", "secondName7", "7", Role.USER);
        UserEntity user8 = new UserEntity("email8", "name8", "secondName8", "8", Role.ADMIN);

        List<UserEntity> userEntities = Arrays.asList(user1, user2, user3, user4, user5, user6, user7, user8);
        batchSave(userEntities);
    }

    public UserEntity findUserByEmail(String email) {
        UserEntity userEntity;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.createQuery("from UserEntity where email =:email");
            query.setParameter("email", email);
            try {
                userEntity = (UserEntity) query.getSingleResult();
            } catch (NoResultException ex) {
                return null;
            }
            return userEntity;
        } catch (Exception ex) {
            throw new DAOException("Can't find user by email " + email, ex);
        }
    }

    public void deleteUserByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            Query query = session.createQuery("delete UserEntity where email =:email");
            query.setParameter("email", email);
            query.executeUpdate();
            tx.commit();
        } catch (Exception ex) {
            throw new DAOException("Can't delete user by email " + email, ex);
        }
    }
}
