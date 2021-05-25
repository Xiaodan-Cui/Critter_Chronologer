package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> findAllCustomers(){
        return customerRepository.findAll();
    }

    public Customer findCustomerById(Long id){
        Optional<Customer> c = customerRepository.findById(id);
        return Optional.ofNullable(c).isPresent()?c.get() : null;
    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer findOwnerByPet(Pet pet){
        return customerRepository.findByPetsContaining(pet);
    }

    public List<Pet> findPetsByOwner(Long id){
        return customerRepository.findPetsByOwner(id);
    }

//    public List<Schedule> findPetsScheduleByOwner(Long id){
//
//    }

}
