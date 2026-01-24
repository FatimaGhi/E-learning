package dcc.formationservice.Service;

import dcc.formationservice.DTO.CreateSectionRequest;
import dcc.formationservice.DTO.SectionResponse;
import dcc.formationservice.DTO.UpdateSectionRequest;
import dcc.formationservice.Entites.Formation;
import dcc.formationservice.Entites.Section;
import dcc.formationservice.Mapper.SectionMapper;
import dcc.formationservice.Repo.FormationRepository;
import dcc.formationservice.Repo.SectionRepository;
import dcc.formationservice.shared.CustomResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Slf4j
public class SectionService {


    private  SectionRepository sectionRepository;
    private  FormationRepository formationRepository;
    private  SectionMapper sectionMapper;

    public SectionService(SectionRepository sectionRepository, FormationRepository formationRepository, SectionMapper sectionMapper) {
        this.formationRepository = formationRepository;
        this.sectionRepository = sectionRepository;
        this.sectionMapper = sectionMapper;

    }

    @Transactional
    public SectionResponse createSection(CreateSectionRequest request) {
        Formation formation = formationRepository.findById(request.getFormationId())
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Training unavailable"));


        Section section = sectionMapper.toEntity(request, formation);
        section = sectionRepository.save(section);

        log.info("section created : {}", section.getId());

        return sectionMapper.toResponse(section);
    }

    @Transactional
    public SectionResponse updateSection(UUID id, UpdateSectionRequest request) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("unavailable section"));


        sectionMapper.updateEntity(section, request);
        section = sectionRepository.save(section);

        return sectionMapper.toResponse(section);
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> getSectionsByFormation(UUID formationId) {
        return sectionRepository.findByFormationIdOrderByOrderIndexAsc(formationId)
                .stream()
                .map(sectionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSection(UUID id) {
        Section section = sectionRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Section not found"));

        sectionRepository.delete(section);
        log.info("deleted section: {}", id);
    }
}
