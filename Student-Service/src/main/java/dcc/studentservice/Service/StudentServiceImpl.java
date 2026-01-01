package dcc.studentservice.Service;

import dcc.studentservice.DTO.StudentRequestDTO;
import dcc.studentservice.DTO.StudentResponseDTO;
import dcc.studentservice.Entities.Student;
import dcc.studentservice.Mapper.StudentMapper;
import dcc.studentservice.Repo.StudentRepository;
import dcc.studentservice.interfaces.StudentService;
import dcc.studentservice.shared.CustomResponseException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class StudentServiceImpl  implements StudentService {

    private StudentRepository studentRepository;
    private StudentMapper studentMapper;

    public StudentServiceImpl(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }



    @Override
    public StudentResponseDTO createStudent(StudentRequestDTO requestDTO) {
        // Check if email already exists
        studentRepository.findByEmail(requestDTO.getEmail())
                .ifPresent(s -> { throw CustomResponseException.Conflict("Email already exists"); });

        Student student = studentMapper.toEntity(requestDTO);
        Student savedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(savedStudent);
    }

    @Override
    public StudentResponseDTO getStudentById(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Student not found"));
        return studentMapper.toResponseDTO(student);
    }

    @Override
    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll()
                .stream()
                .map(studentMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public StudentResponseDTO enableStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Student not found"));
        student.setEnabled(true);
        return studentMapper.toResponseDTO(studentRepository.save(student));
    }

    @Override
    public void deleteStudent(UUID id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Student not found"));
        studentRepository.delete(student);
    }

    @Override
    public StudentResponseDTO updateStudent(UUID id, StudentRequestDTO requestDTO) {

        Student student = studentRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Student not found"));

        if (requestDTO.getFirstName() != null && !requestDTO.getFirstName().isBlank()) {
            student.setFirstName(requestDTO.getFirstName());
        }

        if (requestDTO.getLastName() != null && !requestDTO.getLastName().isBlank()) {
            student.setLastName(requestDTO.getLastName());
        }

        if (requestDTO.getPhone() != null && !requestDTO.getPhone().isBlank()) {
            student.setPhone(requestDTO.getPhone());
        }

        if (requestDTO.getBirthDate() != null) {
            student.setBirthDate(requestDTO.getBirthDate());
        }


        Student updatedStudent = studentRepository.save(student);
        return studentMapper.toResponseDTO(updatedStudent);
    }

}
