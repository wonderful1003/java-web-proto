package com.example.demo.repository;

import com.example.demo.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByVisibleTrueOrderBySortOrder();

    @Query("SELECT DISTINCT m FROM Menu m JOIN m.roles r WHERE r.name IN :roleNames AND m.visible = true ORDER BY m.sortOrder")
    List<Menu> findByRoleNamesAndVisible(@Param("roleNames") List<String> roleNames);
}
