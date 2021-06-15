package com.example.restfulapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents(){
        return studentRepository.findAll();
    }

    public void addStudent(Student student){
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Student already exists");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId){
        if(!studentRepository.existsById(studentId)){
            throw new IllegalStateException("Student not found");
        }
        studentRepository.deleteById(studentId);
    }

    @Transactional
    public void updateStudent(Long studentId, String name, String email, LocalDate dob){

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new IllegalStateException("Student not found"));

        if(!student.getName().equals(name) && name != null){
            student.setName(name);
        }

        Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
        if(studentOptional.isPresent()){
            throw new IllegalStateException("Email already exists");
        } else if(!student.getEmail().equals(email) && email != null){
            student.setEmail(email);
        }

        if(dob != null && !student.getDob().isEqual(dob)){
            student.setDob(dob);
        }
    }

}
