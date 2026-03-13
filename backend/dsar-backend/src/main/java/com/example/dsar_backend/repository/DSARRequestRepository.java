package com.example.dsar_backend.repository;

import com.example.dsar_backend.model.DSARRequest;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DSARRequestRepository extends JpaRepository<DSARRequest, Long>
{

}
