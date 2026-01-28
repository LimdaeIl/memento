package com.natural.memento.user.domain.repository;

import com.natural.memento.user.domain.entity.SocialType;
import com.natural.memento.user.domain.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<User, Long> {

    boolean existsByEmailAndDeletedAtIsNull(String email);

    boolean existsByEmail(String email);

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    Optional<User> findBySocialTypeAndSocialIdAndDeletedAtIsNull(SocialType socialType, String socialId);

}
