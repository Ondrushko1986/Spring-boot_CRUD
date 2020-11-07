package test.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import test.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserDAOImpl extends JpaRepository<User, Integer> {
    User findByName(String name);

    User getById(int id);

}
