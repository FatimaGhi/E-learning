package dcc.studentservice.Controller;


import dcc.studentservice.DTO.StudentRequestDTO;
import dcc.studentservice.DTO.StudentResponseDTO;
import dcc.studentservice.interfaces.StudentService;
import dcc.studentservice.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/students")
public class StudentController {


    private StudentService studentService;
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create student
    @PostMapping
    public ResponseEntity<GlobalResponse<StudentResponseDTO>> createStudent(
            @Valid @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO student = studentService.createStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(student));
    }

    // Get student by ID
    @GetMapping("/{id}")
    public ResponseEntity<GlobalResponse<StudentResponseDTO>> getStudent(
            @PathVariable UUID id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse<>(student));
    }

    // Get all students
    @GetMapping
    public ResponseEntity<GlobalResponse<List<StudentResponseDTO>>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse<>(students));
    }

    // Update student
    // Partial update - PATCH
    @PatchMapping("/{id}")
    public ResponseEntity<GlobalResponse<StudentResponseDTO>> partialUpdateStudent(
            @PathVariable UUID id,
            @RequestBody StudentRequestDTO requestDTO) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, requestDTO);
        return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse<>(updatedStudent));
    }

    // Enable student
    @PutMapping("/enable/{id}")
    public ResponseEntity<GlobalResponse<StudentResponseDTO>> enableStudent(@PathVariable UUID id) {
        StudentResponseDTO student = studentService.enableStudent(id);
        return ResponseEntity.status(HttpStatus.OK).body(new GlobalResponse<>(student));
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<GlobalResponse<Void>> deleteStudent(@PathVariable UUID id) {
        studentService.deleteStudent(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new GlobalResponse<>(null));
    }
}
