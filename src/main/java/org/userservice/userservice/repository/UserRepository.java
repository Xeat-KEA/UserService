package org.userservice.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.userservice.userservice.domain.User;

public interface UserRepository extends JpaRepository<User, String> {

    void findStatisticsByUserId(String userId);

}
