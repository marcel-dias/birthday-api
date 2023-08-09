package com.marceldias.birthday.user;

import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, String> {

    @Query(value = "SELECT * FROM users WHERE name = :name")
    User findByName(@Param("name") String name);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (name, date_of_birth) VALUES (:#{#user.name}, :#{#user.dateOfBirth})")
    void insert(@Param("user") User user);
}
