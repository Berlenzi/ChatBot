package dev.marco.chatbot_site.Repository;

import dev.marco.chatbot_site.Model.MyAppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<MyAppUser,Integer> {

    Optional<MyAppUser> findByUsername(String username);
    List<MyAppUser> findByStatus(String status);


}
