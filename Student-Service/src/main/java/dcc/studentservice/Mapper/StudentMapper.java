package dcc.studentservice.Mapper;


import dcc.studentservice.DTO.StudentRequestDTO;
import dcc.studentservice.DTO.StudentResponseDTO;
import dcc.studentservice.Entities.Student;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    public Student toEntity(StudentRequestDTO dto) {
        return Student.builder()
                .keycloakUserId(dto.getKeycloakUserId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .birthDate(dto.getBirthDate())
                .phone(dto.getPhone())
                .enabled(false) // default for verfiy email
                .build();
    }

    public StudentResponseDTO toResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .keycloakUserId(student.getKeycloakUserId())
                .firstName(student.getFirstName())
                .lastName(student.getLastName())
                .email(student.getEmail())
                .birthDate(student.getBirthDate())
                .phone(student.getPhone())
                .enabled(student.getEnabled())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}
