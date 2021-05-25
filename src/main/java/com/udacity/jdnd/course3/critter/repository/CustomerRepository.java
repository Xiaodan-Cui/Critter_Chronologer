package com.udacity.jdnd.course3.critter.repository;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("select c.pets from Customer c where c.id = :id")
    public List<Pet> findPetsByOwner(Long id);

    public Customer findByPetsContaining(Pet pet);

}
