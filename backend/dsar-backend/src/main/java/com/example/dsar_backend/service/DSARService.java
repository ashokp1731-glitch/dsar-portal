package com.example.dsar_backend.service;

import com.example.dsar_backend.model.AuditLog;
import com.example.dsar_backend.model.DSARRequest;
import com.example.dsar_backend.repository.AuditLogRepository;
import com.example.dsar_backend.repository.DSARRequestRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class DSARService {

    private final DSARRequestRepository dsarRequestRepository;
    private final AuditLogRepository auditLogRepository;

    public DSARService(DSARRequestRepository dsarRequestRepository,
                       AuditLogRepository auditLogRepository) {
        this.dsarRequestRepository = dsarRequestRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public DSARRequest createRequest(DSARRequest request, String username) {
        request.setStatus("SUBMITTED");
        request.setCreatedAt(LocalDateTime.now());

        DSARRequest saved = dsarRequestRepository.save(request);

        saveAudit("Created request ID " + saved.getId(), username);

        return saved;
    }

    public List<DSARRequest> getAllRequests(String username) {
        saveAudit("Viewed all requests", username);
        return dsarRequestRepository.findAll();
    }

    public DSARRequest updateStatus(Long id, String status, String username) {
        DSARRequest request = dsarRequestRepository.findById(id).orElseThrow();

        request.setStatus(status);

        DSARRequest updated = dsarRequestRepository.save(request);

        saveAudit("Updated request " + id + " to " + status, username);

        return updated;
    }

    public List<AuditLog> getAuditLogs(String username) {
        saveAudit("Viewed audit logs", username);
        return auditLogRepository.findAll();
    }

    private void saveAudit(String action, String username) {
        AuditLog log = new AuditLog();

        log.setAction(action);
        log.setPerformedBy(username);
        log.setTimestamp(LocalDateTime.now());

        auditLogRepository.save(log);
    }
}