package com.cruxbackend.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cruxbackend.backend.model.History;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long>{
 History findByCid(long cid);
}
