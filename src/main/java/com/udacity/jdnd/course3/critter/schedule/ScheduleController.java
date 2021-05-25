package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
public class ScheduleController {
    @Autowired
    ScheduleService scheduleService;
    @Autowired
    PetService petService;
    @Autowired
    CustomerService customerService;
    @Autowired
    EmployeeService employeeService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = toSchedule(scheduleDTO);
        Schedule savedSchedule = scheduleService.saveSchedule(schedule);
        return converToSchedleDTO(savedSchedule);
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAll();
        return toDTOS(schedules) ;
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        Pet pet = petService.findPet(petId);
        return toDTOS(scheduleService.findAllByPetsContaining(pet));
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findById(employeeId);
        return toDTOS(scheduleService.findAllByEmployeesContaining(employee));
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        Customer customer = customerService.findCustomerById(customerId);
        List<Pet> pets = customer.getPets();
        Set<Schedule> set = new HashSet();
        for(Pet p : pets){
            List<Schedule> schedules = scheduleService.findAllByPetsContaining(p);
            for(Schedule s : schedules){
                set.add(s);
            }
        }
        List<Schedule> schedules = new ArrayList<>(set);
        return toDTOS(schedules);
    }

    private ScheduleDTO converToSchedleDTO(Schedule schedule){
        List<Employee> employees = schedule.getEmployees();
        List<Pet> pets = schedule.getPets();
        List<Long> employeeIds = new ArrayList();
        List<Long> petIds = new ArrayList();
        for(Employee e : employees){
            employeeIds.add(e.getId());
        }
        for(Pet p :pets){
            petIds.add(p.getId());
        }
        return new ScheduleDTO(schedule.getId(),employeeIds,petIds, schedule.getDate(), schedule.getActivities());
    }

    private Schedule toSchedule(ScheduleDTO scheduleDTO){

        List<Long> employeeIds = scheduleDTO.getEmployeeIds();
        List<Long> petIds = scheduleDTO.getPetIds();
        List<Employee> employees = new ArrayList<>();
        List<Pet> pets = new ArrayList<>();
        for(Long e : employeeIds){
            employees.add(employeeService.findById(e));
        }
        for(Long p :petIds){
            pets.add(petService.findPet(p));
        }
        //if (scheduleDTO.getId() == null){
        return new Schedule(null, employees, pets, scheduleDTO.getDate(),scheduleDTO.getActivities());
//        }
//        else{
//            Schedule schedule = scheduleService.findById(scheduleDTO.getId());
//            schedule.setActivities(scheduleDTO.getActivities());
//            schedule.setDate(scheduleDTO.getDate());
//            schedule.setEmployees(employees);
//            schedule.setPets(pets);
//            return schedule;
//        }
    }

    private List<ScheduleDTO> toDTOS(List<Schedule> schedules){
        List<ScheduleDTO> scheduleDTOS = new ArrayList();
        for(Schedule s : schedules){
            scheduleDTOS.add(converToSchedleDTO(s));
        }
        return scheduleDTOS;
    }
}
