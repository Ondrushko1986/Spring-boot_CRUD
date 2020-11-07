package test.dao;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import test.model.Role;


@Repository
public interface RoleDAOImp extends JpaRepository<Role,Long> {
}
