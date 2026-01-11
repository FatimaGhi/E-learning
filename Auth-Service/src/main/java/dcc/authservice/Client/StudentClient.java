package dcc.authservice.Client;


import dcc.authservice.DTO.StudentRequestDTO;
import dcc.authservice.DTO.StudentResponseDTO;
import dcc.authservice.shared.GlobalResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Student-Service", url = "http://localhost:8081")
public interface StudentClient {

    @PostMapping("/api/students")
    GlobalResponse<StudentResponseDTO> createStudent(@RequestBody StudentRequestDTO dto);

}
