package dcc.formationservice.Mapper;

import dcc.formationservice.DTO.CreateSectionRequest;
import dcc.formationservice.DTO.SectionResponse;
import dcc.formationservice.DTO.UpdateSectionRequest;
import dcc.formationservice.Entites.Formation;
import dcc.formationservice.Entites.Section;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class SectionMapper {
    private SessionMapper  sessionMapper;
    public SectionMapper(SessionMapper  sessionMapper) {
        this.sessionMapper = sessionMapper;
    }

    public Section toEntity(CreateSectionRequest request, Formation formation) {
        return Section.builder()
                .name(request.getName())
                .description(request.getDescription())
                .orderIndex(request.getOrderIndex())
                .formation(formation)
                .build();
    }


    public void updateEntity(Section section, UpdateSectionRequest request) {
        if (request.getName() != null) {
            section.setName(request.getName());
        }
        if (request.getDescription() != null) {
            section.setDescription(request.getDescription());
        }
        if (request.getOrderIndex() != null) {
            section.setOrderIndex(request.getOrderIndex());
        }
    }


    public SectionResponse toResponse(Section section) {
        return SectionResponse.builder()
                .id(section.getId())
                .name(section.getName())
                .description(section.getDescription())
                .orderIndex(section.getOrderIndex())
                .sessions(section.getSessions().stream()
                        .map(sessionMapper::toResponse)
                        .collect(Collectors.toList()))
                .build();
    }
}
