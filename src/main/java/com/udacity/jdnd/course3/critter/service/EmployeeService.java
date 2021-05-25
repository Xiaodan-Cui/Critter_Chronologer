package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeService {

    @Autowired
    EmployeeRepository employeeRepository;

    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    public List<Employee> findEmployee(DayOfWeek day, Set<EmployeeSkill> skills){
        List<Employee> aEmployees = employeeRepository.findAllByDaysAvailableContaining(day);
        List<Employee> res = new ArrayList();
        for(Employee e : aEmployees){
            if (e.getSkills().containsAll(skills)){
                res.add(e);
            }
        }
        return res;
    }

//    public Set<DayOfWeek> checkAvailability(Long id){
//        return employeeRepository.findSchedule(id);
//    }

    public Employee findById(Long id){
        Optional<Employee> e = employeeRepository.findById(id);
        return  e.isPresent()? e.get() : null;
    }


}
