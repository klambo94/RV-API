package com.lamb.kendra.rvapi.MoveCheckList;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
