package com.amazingshop.personal.userservice.repositories;

import com.amazingshop.personal.userservice.models.Person;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
    Optional<Person> findPersonByUsername(@NotEmpty(message = "Username should be not empty")
                                @Size(min = 2, max = 30,
                                        message = "Username should be for 2 to 30 symbols") String username);
}