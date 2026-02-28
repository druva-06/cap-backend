package com.meritcap.service;

import com.meritcap.DTOs.requestDTOs.lead.LeadFilterDto;
import com.meritcap.DTOs.requestDTOs.lead.LeadRequestDto;
import com.meritcap.DTOs.requestDTOs.lead.UpdateLeadRequestDto;
import com.meritcap.DTOs.responseDTOs.lead.LeadPageResponseDto;
import com.meritcap.DTOs.responseDTOs.lead.LeadResponseDto;
import com.meritcap.DTOs.responseDTOs.lead.LeadStatusCountDto;

public interface LeadService {
    LeadResponseDto createLead(LeadRequestDto leadRequestDto, String createdByUserEmail);

    LeadPageResponseDto getLeads(LeadFilterDto filterDto);

    LeadResponseDto getLeadById(Long leadId);

    LeadResponseDto updateLead(Long leadId, UpdateLeadRequestDto updateLeadRequestDto, String updatedByUserEmail);

    Long countTotalLeads();

    LeadStatusCountDto getLeadStatusCounts();
}
