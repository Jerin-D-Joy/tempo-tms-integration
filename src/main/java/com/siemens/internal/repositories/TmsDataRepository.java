package com.siemens.internal.repositories;

import com.siemens.internal.entities.TmsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface TmsDataRepository extends JpaRepository<TmsData, Integer> {

    @Query("DELETE FROM TmsData t WHERE t.rollNo = :rollNo AND CONVERT(DATE, t.dateOfEntry) BETWEEN " +
            "CONVERT(DATE, :startDate) and CONVERT(date, :endDate)")
     long deleteByRollNoAndDates(
            @Param("rollNo") String rollNo,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

    @Modifying
    @Query("UPDATE TmsData t SET t.active=0 WHERE t.rollNo = :rollNo AND t.dateOfEntry BETWEEN :startDate and :endDate")
    int updateByRollNoAndDates(
            @Param("rollNo") String rollNo,
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate);

}
