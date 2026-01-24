package dcc.formationservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionResponse {

    private UUID id;
    private String name;
    private String description;
    private Integer orderIndex;
    private List<SessionResponse> sessions;
}
