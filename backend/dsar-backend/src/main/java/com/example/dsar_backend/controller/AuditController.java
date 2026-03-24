package com.example.dsar_backend.controller;

import com.example.dsar_backend.model.AuditLog;
import com.example.dsar_backend.service.DSARService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "http://localhost:5173")
public class AuditController {

    private final DSARService dsarService;

    public AuditController(DSARService dsarService) {
        this.dsarService = dsarService;
    }

    @GetMapping
    public List<AuditLog> getAuditLogs(Principal principal) {
        return dsarService.getAuditLogs(principal.getName());
    }
}