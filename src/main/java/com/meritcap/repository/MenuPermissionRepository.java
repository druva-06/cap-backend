package com.meritcap.repository;

import com.meritcap.model.MenuPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuPermissionRepository extends JpaRepository<MenuPermission, Long> {

    /**
     * Find all menu permissions for a specific menu
     */
    List<MenuPermission> findByMenuId(String menuId);

    /**
     * Find menu permissions for a specific menu and submenu
     */
    List<MenuPermission> findByMenuIdAndSubmenuId(String menuId, String submenuId);

    /**
     * Find all main menu permissions (where submenuId is null)
     */
    List<MenuPermission> findBySubmenuIdIsNull();

    /**
     * Find all submenu permissions (where submenuId is not null)
     */
    List<MenuPermission> findBySubmenuIdIsNotNull();

    /**
     * Delete all permissions for a specific menu/submenu combination
     */
    @Modifying
    @Query("DELETE FROM MenuPermission mp WHERE mp.menuId = :menuId AND mp.submenuId = :submenuId")
    void deleteByMenuIdAndSubmenuId(@Param("menuId") String menuId, @Param("submenuId") String submenuId);

    /**
     * Check if a menu/submenu has any permissions
     */
    boolean existsByMenuIdAndSubmenuId(String menuId, String submenuId);

    /**
     * Get unique menu IDs
     */
    @Query("SELECT DISTINCT mp.menuId FROM MenuPermission mp WHERE mp.submenuId IS NULL ORDER BY mp.menuId")
    List<String> findDistinctMenuIds();

    /**
     * Get submenu IDs for a specific menu
     */
    @Query("SELECT DISTINCT mp.submenuId FROM MenuPermission mp WHERE mp.menuId = :menuId AND mp.submenuId IS NOT NULL ORDER BY mp.submenuId")
    List<String> findDistinctSubmenuIdsByMenuId(String menuId);
}
