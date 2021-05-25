package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
public class PetController {
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        Pet pet = convertToPet(petDTO);
        Pet savedPet = petService.savePet(pet);
        return convertToPetDTO(savedPet);
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        return convertToPetDTO(pet);
    }

    @GetMapping
    public List<PetDTO> getPets(){
        List<Pet> pets = petService.findAllPets();
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet p : pets){
            petDTOS.add(convertToPetDTO(p));
        }
        return petDTOS;
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        Customer customer = customerService.findCustomerById(ownerId);
        List<Pet> pets = petService.findPetsByOwner(customer);
        List<PetDTO> petDTOS = new ArrayList<>();
        for(Pet p : pets){
            petDTOS.add(convertToPetDTO(p));
        }
        return petDTOS;
    }

    private PetDTO convertToPetDTO(Pet pet){
        return new PetDTO(pet.getId(), pet.getType(),pet.getName(),pet.getOwner().getId(), pet.getBirthDate(),pet.getNotes());
    }

    private Pet convertToPet(PetDTO petDTO){
        Customer owner = petDTO.getOwnerId() == null? null : customerService.findCustomerById(petDTO.getOwnerId());
        //if(petDTO.getId() == null){
//        Long id = null;
//        if (petDTO.getId() != null && petService.findPet(petDTO.getId()) == null){
//            id = petDTO.getId();
//        }
            return new Pet(null, petDTO.getType(),petDTO.getName(),owner,petDTO.getBirthDate(), petDTO.getNotes());
        //}
//        else{
//            Pet pet = petService.findPet(petDTO.getId());
//            pet.setType(petDTO.getType());
//            pet.setOwner(owner);
//            pet.setBirthDate(petDTO.getBirthDate());
//            pet.setName(petDTO.getName());
//            pet.setNotes(petDTO.getNotes());
//            return pet;
//        }
    }
}
