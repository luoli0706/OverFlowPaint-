package com.example.overflowpaint.repository;

import com.example.overflowpaint.model.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
//AI生成，应当是与数据库交互有关
@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
}

