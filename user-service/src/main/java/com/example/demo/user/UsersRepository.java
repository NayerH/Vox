package com.example.demo.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long>{

   // @Query ("SELECT u FROM Users u WHERE u.email =?1")
    Optional <Users> findUsersByEmail(String email);


    @Query("""
        select u from Users u
        where u.id = :user_id
        """)
    Optional<Users> findUsersById(@Param("user_id") Integer id);

    Users findByUsername(String username);

    boolean existsByEmail(String userEmail);
}
