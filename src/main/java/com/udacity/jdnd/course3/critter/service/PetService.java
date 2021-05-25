package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PetService {
    @Autowired
    PetRepository petRepository;
    @Autowired
    CustomerRepository customerRepository;

    public List<Pet> findAllPets(){
        return petRepository.findAll();
    }

    public Pet savePet(Pet pet){
        Pet savedPet = petRepository.save(pet);
        Customer customer = savedPet.getOwner();
        List<Pet> pets = customer.getPets();
        if (pets == null) pets = new ArrayList();
        pets.add((savedPet));
        customer.setPets(pets);
        customerRepository.save(customer);
        return savedPet;
    }

    public Pet findPet(Long id){
        Optional<Pet> p = petRepository.findById(id);
        return p.isPresent()? p.get(): null;
    }


    public List<Pet> findPetsByOwner(Customer customer){
        return petRepository.findAllByOwner(customer);
    }
}
