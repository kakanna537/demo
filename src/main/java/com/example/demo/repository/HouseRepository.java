package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import com.example.demo.model.House;
import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    // 基本查询
    List<House> findByUsername(String username);

    Optional<House> findById(Long id);

    List<House> findAll();

    // 按地址模糊查询
    List<House> findByAddressContaining(String address);

    // 按类型查询
    List<House> findByType(String type);

    // 按价格范围查询
    List<House> findByPriceBetween(int minPrice, int maxPrice);

    List<House> findByPriceLessThan(int price);

    List<House> findByPriceGreaterThan(int price);

    // 按面积范围查询
    List<House> findByAreaBetween(int minArea, int maxArea);

    List<House> findByAreaLessThan(int area);

    List<House> findByAreaGreaterThan(int area);

    // 按租赁状态查询
    List<House> findByIsRented(Boolean isRented);

    // 组合查询
    @Query("SELECT h FROM House h WHERE " +
            "(:type is null OR h.type = :type) AND " +
            "(:minPrice is null OR h.price >= :minPrice) AND " +
            "(:maxPrice is null OR h.price <= :maxPrice) AND " +
            "(:minArea is null OR h.area >= :minArea) AND " +
            "(:maxArea is null OR h.area <= :maxArea) AND " +
            "(:isRented is null OR h.isRented = :isRented) AND " +
            "(:searchQuery is null OR " +
            "LOWER(h.title) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(h.address) LIKE LOWER(CONCAT('%', :searchQuery, '%')) OR " +
            "LOWER(h.description) LIKE LOWER(CONCAT('%', :searchQuery, '%')))")
    List<House> findByFilters(@Param("type") String type,
            @Param("minPrice") Integer minPrice,
            @Param("maxPrice") Integer maxPrice,
            @Param("minArea") Integer minArea,
            @Param("maxArea") Integer maxArea,
            @Param("isRented") Boolean isRented,
            @Param("searchQuery") String searchQuery);

    // 查找附近的房屋（简化版）
    @Query("SELECT h FROM House h WHERE h.isRented = false")
    List<House> findNearbyHouses(@Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("radius") double radiusInKm);

    @Modifying
    @Query("UPDATE House h SET h.address = :address, h.location.latitude = :latitude, h.location.longitude = :longitude WHERE h.id = :id")
    int updateLocation(@Param("id") Long id, @Param("address") String address, @Param("latitude") double latitude, @Param("longitude") double longitude);

    @Modifying
    @Query("UPDATE House h SET h.price = :price WHERE h.id = :id")
    int updatePrice(@Param("id") Long id, @Param("price") int price);

    @Modifying
    @Query("UPDATE House h SET h.isRented = :isRented WHERE h.id = :id")
    int updateRentalStatus(@Param("id") Long id, @Param("isRented") boolean isRented);
}