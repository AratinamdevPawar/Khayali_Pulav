package com.project.khayalipulao.repository;

import com.project.khayalipulao.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByProviderId(int providerId);
    List<MenuItem> findByDateAdded(LocalDate date);
}