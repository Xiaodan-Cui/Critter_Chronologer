package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleService {
    @Autowired
    ScheduleRepository scheduleRepository;

    public List<Schedule> findAll(){
        return scheduleRepository.findAll();
    }

    public List<Schedule> findAllByPetsContaining(Pet pet){
        return scheduleRepository.findAllByPetsContaining(pet);
    }

    public List<Schedule> findAllByEmployeesContaining(Employee employee){
        return scheduleRepository.findAllByEmployeesContaining(employee);
    }

    public Schedule findById(Long id){
        Optional<Schedule> s = scheduleRepository.findById(id);
        return s.isPresent()? s.get(): null;
    }

    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }
}
