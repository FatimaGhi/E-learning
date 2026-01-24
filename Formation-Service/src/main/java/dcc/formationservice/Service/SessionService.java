package dcc.formationservice.Service;


import dcc.formationservice.DTO.CreateSessionRequest;
import dcc.formationservice.DTO.SessionResponse;
import dcc.formationservice.DTO.UpdateSessionRequest;
import dcc.formationservice.Entites.Section;
import dcc.formationservice.Entites.Session;
import dcc.formationservice.Mapper.SessionMapper;
import dcc.formationservice.Repo.SectionRepository;
import dcc.formationservice.Repo.SessionRepository;
import dcc.formationservice.shared.CustomResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import tools.jackson.databind.ser.jdk.UUIDSerializer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class SessionService {


    private  SessionRepository sessionRepository;
    private  SectionRepository sectionRepository;
    private  MinIOService minIOService;
    private  SessionMapper sessionMapper;

    public SessionService(SessionRepository sessionRepository, SectionRepository sectionRepository,MinIOService minIOService, SessionMapper sessionMapper) {
        this.minIOService = minIOService;
        this.sessionRepository = sessionRepository;
        this.sessionMapper =sessionMapper;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public SessionResponse createSession(CreateSessionRequest request, MultipartFile contentFile) {
        Section section = sectionRepository.findById(request.getSectionId())
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Section introuvable"));

        Session.SessionType type = Session.SessionType.valueOf(request.getType());

        // Upload le fichier vers MinIO
        String contentUrl = null;
        if (contentFile != null && !contentFile.isEmpty()) {
            String folder = type == Session.SessionType.VIDEO ?
                    "formations/videos" : "formations/files";
            contentUrl = minIOService.uploadFile(contentFile, folder);
        }

        // Use the mapper to create the entity
        Session session = sessionMapper.toEntity(request, section, contentUrl);
        session = sessionRepository.save(session);

        log.info("session created: {}", session.getId());

        return sessionMapper.toResponse(session);
    }

    @Transactional
    public SessionResponse updateSession(UUID id, UpdateSessionRequest request, MultipartFile contentFile) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Session not found"));


        sessionMapper.updateEntity(session, request);

        if (contentFile != null && !contentFile.isEmpty()) {
            if (session.getContentUrl() != null) {
                minIOService.deleteFile(session.getContentUrl());
            }

            String folder = session.getType() == Session.SessionType.VIDEO ?
                    "formations/videos" : "formations/files";
            String newContentUrl = minIOService.uploadFile(contentFile, folder);
            session.setContentUrl(newContentUrl);
        }

        session = sessionRepository.save(session);
        return sessionMapper.toResponse(session);
    }

    @Transactional(readOnly = true)
    public List<SessionResponse> getSessionsBySection(UUID sectionId) {
        return sessionRepository.findBySectionIdOrderByOrderIndexAsc(sectionId)
                .stream()
                .map(sessionMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteSession(UUID id) {
        Session session = sessionRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Session not found"));

        if (session.getContentUrl() != null) {
            minIOService.deleteFile(session.getContentUrl());
        }

        sessionRepository.delete(session);
        log.info("Session deleted: {}", id);
    }
}
