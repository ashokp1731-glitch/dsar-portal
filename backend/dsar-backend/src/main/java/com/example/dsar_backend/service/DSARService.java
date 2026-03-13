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

    private final DSARRequestRepository requestRepository;
    private final AuditLogRepository auditLogRepository;

    public DSARService(DSARRequestRepository requestRepository, AuditLogRepository auditLogRepository) {
        this.requestRepository = requestRepository;
        this.auditLogRepository = auditLogRepository;
    }

    public DSARRequest createRequest(DSARRequest request, String username) {
        request.setStatus("SUBMITTED");
        request.setCreatedAt(LocalDateTime.now());
        DSARRequest saved = requestRepository.save(request);
        saveAudit("Created DSAR request", username);
        return saved;
    }

    public List<DSARRequest> getAllRequests(String username) {
        saveAudit("Viewed all DSAR requests", username);
        return requestRepository.findAll();
    }

    public DSARRequest updateStatus(Long id, String status, String username) {
        DSARRequest request = requestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Request not found"));

        request.setStatus(status);
        DSARRequest updated = requestRepository.save(request);
        saveAudit("Updated DSAR request status to " + status, username);
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
