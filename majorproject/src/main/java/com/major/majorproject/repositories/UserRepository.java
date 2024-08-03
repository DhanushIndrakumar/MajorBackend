package com.major.majorproject.repositories;

import com.major.majorproject.entities.Role;
import com.major.majorproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    Optional<User> findByUserId(int userId);

    Optional<User> findByEmail(String email);

    User findByRole(Role role);



    // Query method to find a User by userName
    Optional<User> findByUserName(String userName);


}
