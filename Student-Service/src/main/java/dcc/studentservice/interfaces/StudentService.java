package dcc.studentservice.interfaces;


import dcc.studentservice.DTO.StudentRequestDTO;
import dcc.studentservice.DTO.StudentResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface StudentService {

    StudentResponseDTO createStudent(StudentRequestDTO requestDTO);

    StudentResponseDTO getStudentById(UUID id);

    List<StudentResponseDTO> getAllStudents();

    StudentResponseDTO enableStudent(UUID id);

    void deleteStudent(UUID id);

    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO);
}
