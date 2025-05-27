package com.example.demo.controller;

import com.example.demo.model.House;
import com.example.demo.model.Owner;
import com.example.demo.model.Location;
import com.example.demo.dto.CreateHouseRequest;
import com.example.demo.dto.HouseDTO;
import com.example.demo.service.HouseService;
import com.example.demo.service.OwnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/houses")
@CrossOrigin(origins = "http://localhost:5173")
public class HouseController {
    @Autowired
    private HouseService houseService;

    @Autowired
    private OwnerService ownerService;

    @GetMapping
    public List<House> getAllHouses() {
        return houseService.findAll();
    }

    @PostMapping
    public House createHouse(@RequestBody CreateHouseRequest request) {
        House house = new House();
        house.setTitle(request.getTitle());
        house.setDescription(request.getDescription());
        house.setType(request.getType());
        house.setRooms(request.getRooms());
        house.setFloor(request.getFloor());
        house.setOrientation(request.getOrientation());
        house.setYear(request.getYear());
        house.setArea(request.getArea());
        house.setPrice(request.getPrice());
        house.setImage(request.getImage());
        house.setAddress(request.getAddress());
        house.setUsername(request.getUsername());
        
        Location location = new Location(request.getLatitude(), request.getLongitude());
        house.setLocation(location);
        
        return houseService.save(house);
    }

    @GetMapping("/owner/{username}")
    public List<House> getHousesByUsername(@PathVariable String username) {
        return houseService.getHousesByUsername(username);
    }

    @GetMapping("/{id}")
    public ResponseEntity<HouseDTO> getHouseById(@PathVariable Long id) {
        Optional<House> houseOptional = houseService.getHouseById(id);
        if (!houseOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        House house = houseOptional.get(); // 从 Optional 中获取 House 对象

        // 根据 house 的 username 查询 owner 信息
        Owner owner = ownerService.getOwnerByUsername(house.getUsername());
        if (owner == null) {
            return ResponseEntity.notFound().build();
        }

        // 构建一个 DTO 来包含 house 和 owner 的信息
        HouseDTO houseDTO = new HouseDTO(house, owner);

        return ResponseEntity.ok(houseDTO);
    }

    // 查找附近的房屋
    @GetMapping("/nearby")
    public ResponseEntity<List<House>> findNearbyHouses(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5.0") double radius) {
        List<House> nearbyHouses = houseService.findNearbyHouses(latitude, longitude, radius);
        return ResponseEntity.ok(nearbyHouses);
    }

    // 按地址搜索房屋
    @GetMapping("/search")
    public ResponseEntity<List<House>> searchHousesByAddress(@RequestParam String address) {
        List<House> houses = houseService.searchHousesByAddress(address);
        return ResponseEntity.ok(houses);
    }

    @PatchMapping("/{id}/rental-status")
    public ResponseEntity<House> updateRentalStatus(@PathVariable Long id, @RequestBody Map<String, Boolean> rentalStatus) {
        House updatedHouse = houseService.updateRentalStatus(id, rentalStatus.get("isRented"));
        return ResponseEntity.ok(updatedHouse);
    }

    @PutMapping("/{id}/location")
    public ResponseEntity<?> updateLocation(
            @PathVariable Long id,
            @RequestParam String address,
            @RequestParam double latitude,
            @RequestParam double longitude) {
        boolean success = houseService.updateLocation(id, address, latitude, longitude);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "房屋位置信息更新成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "房屋位置信息更新失败"));
    }

    @PutMapping("/{id}/price")
    public ResponseEntity<?> updatePrice(
            @PathVariable Long id,
            @RequestParam int price) {
        boolean success = houseService.updatePrice(id, price);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "房屋价格更新成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "房屋价格更新失败"));
    }

    @PutMapping("/{id}/rental-status")
    public ResponseEntity<?> updateRentalStatus(
            @PathVariable Long id,
            @RequestParam boolean isRented) {
        boolean success = houseService.updateRentalStatus(id, isRented);
        if (success) {
            return ResponseEntity.ok().body(Map.of("message", "房屋租赁状态更新成功"));
        }
        return ResponseEntity.badRequest().body(Map.of("message", "房屋租赁状态更新失败"));
    }

    @PutMapping("/{id}/restore")
    public ResponseEntity<?> restoreHouse(@PathVariable Long id) {
        houseService.restoreHouseById(id);
        return ResponseEntity.ok().body(Map.of("message", "房源已恢复"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteHouse(@PathVariable Long id) {
        houseService.markHouseAsDeleted(id);
        return ResponseEntity.ok().body(Map.of("message", "房源已删除"));
    }

    @DeleteMapping("/{id}/force")
    public ResponseEntity<?> forceDeleteHouse(@PathVariable Long id) {
        houseService.deleteHouseById(id);
        return ResponseEntity.ok().body(Map.of("message", "房源已彻底删除"));
    }
}
