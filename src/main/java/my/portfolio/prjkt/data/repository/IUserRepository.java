package my.portfolio.prjkt.data.repository;

import my.portfolio.prjkt.data.entities.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<MyUser, Integer> {

    Optional<MyUser> findByUserName(String username);

    MyUser getByUserName(String username);

}
