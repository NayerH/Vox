package com.example.demo.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Integer> {
    @Query("""
             select t from Token t inner join Users u on t.user.id = u.id
            where u.id=:user_id and (t.expired=false or t.revoked = false)
            """)
    List<Token> findAllValidTokensByUser(Integer user_id);

    Optional<Token> findByToken(String token);
}
