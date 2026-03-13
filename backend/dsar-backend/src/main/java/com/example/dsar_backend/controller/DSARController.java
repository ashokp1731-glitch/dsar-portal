package com.example.dsar_backend.controller;

import com.example.dsar_backend.model.DSARRequest;
import com.example.dsar_backend.service.DSARService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "http://localhost:5173")
public class DSARController {

    private final DSARService dsarService;

    public DSARController(DSARService dsarService) {
        this.dsarService = dsarService;
    }

    @PostMapping
    public DSARRequest createRequest(@RequestBody DSARRequest request, Principal principal) {
        return dsarService.createRequest(request, principal.getName());
    }

    @GetMapping
    public List<DSARRequest> getAllRequests(Principal principal) {
        return dsarService.getAllRequests(principal.getName());
    }

    @PutMapping("/{id}")
    public DSARRequest updateStatus(@PathVariable Long id,
                                    @RequestParam String status,
                                    Principal principal) {
        return dsarService.updateStatus(id, status, principal.getName());
    }
}
