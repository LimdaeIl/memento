package com.natural.memento.user.domain.repository;

import com.natural.memento.user.domain.entity.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInterestJpaRepository extends JpaRepository<UserInterest, Long> {

}
