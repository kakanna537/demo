package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.House;
import com.example.demo.repository.HouseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class HouseService {
    private static final Logger logger = LoggerFactory.getLogger(HouseService.class);

    @Autowired
    private HouseRepository houseRepository;

    public List<House> findAll() {
        // 只返回未被删除的房源
        return houseRepository.findAll().stream()
            .filter(house -> house.getIsDeleted() == null || !house.getIsDeleted())
            .toList();
    }

    public House save(House house) {
        return houseRepository.save(house);
    }

    public List<House> getHousesByUsername(String username) {
        return houseRepository.findByUsername(username);
    }

    public Optional<House> getHouseById(Long id) {
        return houseRepository.findById(id);
    }

    // 查找附近的房屋
    public List<House> findNearbyHouses(double latitude, double longitude, double radiusInKm) {
        return houseRepository.findNearbyHouses(latitude, longitude, radiusInKm);
    }

    // 按地址搜索房屋
    public List<House> searchHousesByAddress(String address) {
        return houseRepository.findByAddressContaining(address);
    }

    // 按类型搜索
    public List<House> findByType(String type) {
        return houseRepository.findByType(type);
    }

    // 价格相关搜索
    public List<House> findByPriceRange(int minPrice, int maxPrice) {
        return houseRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<House> findByMaxPrice(int maxPrice) {
        return houseRepository.findByPriceLessThan(maxPrice);
    }

    public List<House> findByMinPrice(int minPrice) {
        return houseRepository.findByPriceGreaterThan(minPrice);
    }

    // 面积相关搜索
    public List<House> findByAreaRange(int minArea, int maxArea) {
        return houseRepository.findByAreaBetween(minArea, maxArea);
    }

    public List<House> findByMaxArea(int maxArea) {
        return houseRepository.findByAreaLessThan(maxArea);
    }

    public List<House> findByMinArea(int minArea) {
        return houseRepository.findByAreaGreaterThan(minArea);
    }

    // 租赁状态搜索
    public List<House> findByRentalStatus(Boolean isRented) {
        return houseRepository.findByIsRented(isRented);
    }

    // 综合搜索
    public List<House> searchHouses(String type, 
                                  Integer minPrice, 
                                  Integer maxPrice,
                                  Integer minArea,
                                  Integer maxArea,
                                  Boolean isRented,
                                  String searchQuery) {
        return houseRepository.findByFilters(type, minPrice, maxPrice, minArea, maxArea, isRented, searchQuery);
    }

    public House updateRentalStatus(Long id, Boolean isRented) {
        // 获取房屋实例
        House house = houseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("House not found with id " + id));

        // 更新租赁状态
        house.setIsRented(isRented);

        // 保存更新后的房屋实例
        return houseRepository.save(house);
    }

    // 批量保存
    public List<House> saveAll(List<House> houses) {
        return houseRepository.saveAll(houses);
    }

    // 更新位置信息
    @Transactional
    public boolean updateLocation(Long id, String address, double latitude, double longitude) {
        try {
            return houseRepository.updateLocation(id, address, latitude, longitude) > 0;
        } catch (Exception e) {
            logger.error("更新房屋位置信息失败: " + e.getMessage());
            return false;
        }
    }

    // 更新价格
    @Transactional
    public boolean updatePrice(Long id, int price) {
        try {
            return houseRepository.updatePrice(id, price) > 0;
        } catch (Exception e) {
            logger.error("更新房屋价格失败: " + e.getMessage());
            return false;
        }
    }

    // 更新租赁状态
    @Transactional
    public boolean updateRentalStatus(Long id, boolean isRented) {
        try {
            return houseRepository.updateRentalStatus(id, isRented) > 0;
        } catch (Exception e) {
            logger.error("更新房屋租赁状态失败: " + e.getMessage());
            return false;
        }
    }

    public void deleteHouseById(Long id) {
        houseRepository.deleteById(id);
    }

    public void markHouseAsDeleted(Long id) {
        House house = houseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("House not found with id " + id));
        house.setIsDeleted(true);
        houseRepository.save(house);
    }

    public void restoreHouseById(Long id) {
        House house = houseRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("House not found with id " + id));
        house.setIsDeleted(false);
        houseRepository.save(house);
    }
}
