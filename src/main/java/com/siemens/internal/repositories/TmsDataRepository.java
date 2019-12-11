package com.siemens.internal.repositories;

import com.siemens.internal.entities.TmsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TmsDataRepository extends JpaRepository<TmsData, Integer> {

    @Query("DELETE FROM TmsData t WHERE t.rollNo = :rollNo AND CONVERT(DATE, t.dateOfEntry) BETWEEN " +
            "CONVERT(DATE, :startDate) and CONVERT(date, :endDate)")
     long deleteByRollNoAndDates(
            @Param("rollNo") String rollNo,
            @Param("startDate") String startDate,
            @Param("endDate") String endDate);

}
