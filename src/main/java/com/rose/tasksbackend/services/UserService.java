package com.rose.tasksbackend.services;

import com.rose.tasksbackend.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserService extends JpaRepository<User, String> {
    @Query("SELECT COUNT(u) FROM User u WHERE u.username=:username")
    long countUserByUsername(@Param("username") String username);

    default boolean isUsernameExisted(String username) {
        return countUserByUsername(username) > 0;
    }
}
