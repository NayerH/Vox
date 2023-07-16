package com.example.demo.token;

import com.example.demo.content.InappropriateContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<InappropriateContent, Integer> {

    Optional <InappropriateContent> findById(Integer id);
}
