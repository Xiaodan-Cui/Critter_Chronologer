package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;
    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = convertToCustomer(customerDTO);
        Customer savedCustomer = customerService.saveCustomer(customer);
        return converToCustomerDTO(savedCustomer);
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customers = customerService.findAllCustomers();
        List<CustomerDTO> customerDTOS = new ArrayList<>();
        for(Customer c : customers){
            customerDTOS.add(converToCustomerDTO(c));
        }
        return customerDTOS;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        Pet pet = petService.findPet(petId);
        Customer customer = customerService.findOwnerByPet(pet);
        return converToCustomerDTO(customer);
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = convertToEmployee(employeeDTO);
        Employee savedEmployee = employeeService.saveEmployee(employee);
        return converToEmployeeDTO(savedEmployee);
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        return converToEmployeeDTO(employeeService.findById(employeeId));
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        employee.setDaysAvailable(daysAvailable);
        employeeService.saveEmployee(employee);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeRequestDTO) {
        DayOfWeek day = employeeRequestDTO.getDate().getDayOfWeek();
        Set<EmployeeSkill> skills = employeeRequestDTO.getSkills();
        List<Employee> employees = employeeService.findEmployee(day,skills);
        return toEmployeeDTOS(employees);
    }

    private EmployeeDTO converToEmployeeDTO(Employee employee){
        return new EmployeeDTO(employee.getId(),employee.getName(),employee.getSkills(),employee.getDaysAvailable());
    }

    private CustomerDTO converToCustomerDTO(Customer customer){
        List<Pet> pets = customer.getPets();
        List<Long> petIds = new ArrayList<>();
        for(Pet p : pets){
            petIds.add(p.getId());
        }
        return new CustomerDTO(customer.getId(),customer.getName(),customer.getPhoneNumber(),customer.getNotes(), petIds);
    }

    private Customer convertToCustomer(CustomerDTO customerDTO){

        List<Pet> pets = new ArrayList();
        if(customerDTO.getPetIds() != null) {
            List<Long> petIds = customerDTO.getPetIds();

            for (Long id : petIds) {
                pets.add(petService.findPet(id));
            }
        }
//        if (customerDTO.getId() == null){
        return new Customer(null, customerDTO.getName(),customerDTO.getPhoneNumber(),customerDTO.getNotes(),pets);
//        }
//        else{
//            Customer customer = customerService.findCustomerById(customerDTO.getId());
//            customer.setName(customerDTO.getName());
//            customer.setNotes(customerDTO.getNotes());
//            customer.setPets(pets);
//            customer.setPhoneNumber(customerDTO.getPhoneNumber());
//            customer.setName(customer.getNotes());
//            return customer;
//        }
    }

    private Employee convertToEmployee(EmployeeDTO employeeDTO){
       // if (employeeDTO.getId() == null){
        return new Employee(null, employeeDTO.getName(),employeeDTO.getSkills(),employeeDTO.getDaysAvailable());
//        }
//        else{
//            Employee employee = employeeService.findById(employeeDTO.getId());
//            employee.setName(employeeDTO.getName());
//            employee.setDaysAvailable(employeeDTO.getDaysAvailable());
//            employee.setSkills(employeeDTO.getSkills());
//            return employee;
//        }
    }

    private List<EmployeeDTO> toEmployeeDTOS (List<Employee> employees){
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        for(Employee e : employees){
            employeeDTOS.add(converToEmployeeDTO(e));
        }
        return employeeDTOS;
    }

}
