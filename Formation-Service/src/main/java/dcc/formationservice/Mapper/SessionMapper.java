package dcc.formationservice.Mapper;


import dcc.formationservice.DTO.CreateSessionRequest;
import dcc.formationservice.DTO.SessionResponse;
import dcc.formationservice.DTO.UpdateSessionRequest;
import dcc.formationservice.Entites.Section;
import dcc.formationservice.Entites.Session;
import org.springframework.stereotype.Component;

@Component
public class SessionMapper {

    public Session toEntity(CreateSessionRequest request, Section section, String contentUrl) {
        return Session.builder()
                .name(request.getName())
                .description(request.getDescription())
                .orderIndex(request.getOrderIndex())
                .type(Session.SessionType.valueOf(request.getType()))
                .contentUrl(contentUrl)
                .durationMinutes(request.getDurationMinutes())
                .qcmData(request.getQcmData())
                .passingScore(request.getPassingScore())
                .section(section)
                .isActive(true)
                .build();
    }

    public void updateEntity(Session session, UpdateSessionRequest request) {
        if (request.getName() != null) {
            session.setName(request.getName());
        }
        if (request.getDescription() != null) {
            session.setDescription(request.getDescription());
        }
        if (request.getOrderIndex() != null) {
            session.setOrderIndex(request.getOrderIndex());
        }
        if (request.getType() != null) {
            session.setType(Session.SessionType.valueOf(request.getType()));
        }
        if (request.getDurationMinutes() != null) {
            session.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getQcmData() != null) {
            session.setQcmData(request.getQcmData());
        }
        if (request.getPassingScore() != null) {
            session.setPassingScore(request.getPassingScore());
        }
        if (request.getIsActive() != null) {
            session.setIsActive(request.getIsActive());
        }
    }


    public SessionResponse toResponse(Session session) {
        return SessionResponse.builder()
                .id(session.getId())
                .name(session.getName())
                .description(session.getDescription())
                .orderIndex(session.getOrderIndex())
                .type(session.getType().name())
                .contentUrl(session.getContentUrl())
                .durationMinutes(session.getDurationMinutes())
                .qcmData(session.getQcmData())
                .passingScore(session.getPassingScore())
                .isActive(session.getIsActive())
                .build();
    }
}
