package org.antonioxocoy.cecommerce.repository;

import org.antonioxocoy.cecommerce.entity.User;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@EnableScan
@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findById(Long id);

    boolean existsByEmail(String email);

    Optional<User> findFirstByEmail(String email);

}
