package com.cruxbackend.backend.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cruxbackend.backend.model.Chat;
import java.util.List;


@Repository
public interface ChatRepository extends JpaRepository<Chat,Long> {
  List<Chat> findByUid(long uid);
}
