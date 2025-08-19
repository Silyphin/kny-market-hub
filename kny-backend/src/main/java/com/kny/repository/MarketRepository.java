package com.kny.repository;

import com.kny.model.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface MarketRepository extends JpaRepository<Market, Long> {
    
    // Find market by Google Place ID
    Optional<Market> findByGooglePlaceId(String googlePlaceId);
    
    // Find markets by name (case insensitive)
    List<Market> findByNameContainingIgnoreCase(String name);
    
    // Find markets by data source
    List<Market> findByDataSource(Market.DataSource dataSource);
    
    // Find covered markets
    List<Market> findByIsCoveredTrue();
    
    // Find markets by crowd level for specific time periods
    List<Market> findByCrowdLevelMorning(Market.CrowdLevel crowdLevel);
    List<Market> findByCrowdLevelAfternoon(Market.CrowdLevel crowdLevel);
    List<Market> findByCrowdLevelEvening(Market.CrowdLevel crowdLevel);
    
    // Find markets within a certain radius (using Haversine formula)
    @Query("SELECT m FROM Market m WHERE " +
           "(6371 * acos(cos(radians(:latitude)) * cos(radians(m.latitude)) * " +
           "cos(radians(m.longitude) - radians(:longitude)) + " +
           "sin(radians(:latitude)) * sin(radians(m.latitude)))) <= :radiusKm")
    List<Market> findMarketsWithinRadius(@Param("latitude") BigDecimal latitude, 
                                       @Param("longitude") BigDecimal longitude, 
                                       @Param("radiusKm") double radiusKm);
    
    // Find markets by specialties (case insensitive)
    @Query("SELECT m FROM Market m WHERE LOWER(m.specialties) LIKE LOWER(CONCAT('%', :specialty, '%'))")
    List<Market> findBySpecialtiesContaining(@Param("specialty") String specialty);
    
    // Get all markets ordered by name
    List<Market> findAllByOrderByNameAsc();
    
    // REMOVED: Rating-related queries
    
    // Find markets that need Google sync (null or older than specified days)
    @Query("SELECT m FROM Market m WHERE m.lastGoogleSync IS NULL OR m.lastGoogleSync < :cutoffDate")
    List<Market> findMarketsNeedingGoogleSync(@Param("cutoffDate") java.time.LocalDateTime cutoffDate);
    
    // Get market count by data source
    @Query("SELECT m.dataSource, COUNT(m) FROM Market m GROUP BY m.dataSource")
    List<Object[]> countMarketsByDataSource();
}