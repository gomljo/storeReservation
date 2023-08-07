package com.store.reservation.store.store.repository;


import com.store.reservation.store.store.domain.model.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long> {

    boolean existsByStoreName(String storeName);

    Optional<Store> findByStoreName(String storeName);
    Optional<Store> findByStoreId(long storeId);
    @Query("SELECT s " +
            "FROM Store s " +
            "WHERE FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :centerLng, :centerLat), FUNCTION('POINT', s.location.lnt, s.location.lat)) <= :radiusMeters " +
            "ORDER BY FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :centerLng, :centerLat), FUNCTION('POINT', s.location.lnt, s.location.lat)) ASC")
    List<Store> searchStoreByRadiusDistanceASC(@Param("centerLat") double centerLat,
                                               @Param("centerLng") double centerLng,
                                               @Param("radiusMeters") double radiusMeters,
                                               Pageable paging);

    @Query("SELECT s " +
            "FROM Store s " +
            "WHERE FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :centerLng, :centerLat), FUNCTION('POINT', s.location.lnt, s.location.lat)) <= :radiusMeters " +
            "ORDER BY s.storeName ASC ")
    List<Store> searchStoreByRadiusAlphabeticASC(@Param("centerLat") double centerLat,
                                                 @Param("centerLng") double centerLng,
                                                 @Param("radiusMeters") double radiusMeters,
                                                 Pageable paging);

    @Query("SELECT s " +
            "FROM Store s " +
            "WHERE FUNCTION('ST_Distance_Sphere', FUNCTION('POINT', :centerLng, :centerLat), FUNCTION('POINT', s.location.lnt, s.location.lat)) <= :radiusMeters " +
            "ORDER BY s.starRating DESC, s.storeName ASC")
    List<Store> searchStoreByRadiusStarRatingDESC(@Param("centerLat") double centerLat,
                                                  @Param("centerLng") double centerLng,
                                                  @Param("radiusMeters") double radiusMeters,
                                                  Pageable paging);

}
