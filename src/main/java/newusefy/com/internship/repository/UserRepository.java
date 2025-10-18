package newusefy.com.internship.repository;

import newusefy.com.internship.model.User; // импортируем нашу Entity
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // базовые CRUD-методы создаются автоматически
    // тут пока пусто – базовые методы создаются автоматически
    /*this line I added on week3: Spring Data will implement this method for you based on the method name.
     It returns null if no user is found (we’ll check for that).*/
    Optional<User> findByUsername(String username);
}