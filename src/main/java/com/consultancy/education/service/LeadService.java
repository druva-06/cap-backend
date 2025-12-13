package com.consultancy.education.service;

import com.consultancy.education.DTOs.requestDTOs.lead.LeadFilterDto;
import com.consultancy.education.DTOs.requestDTOs.lead.LeadRequestDto;
import com.consultancy.education.DTOs.requestDTOs.lead.UpdateLeadRequestDto;
import com.consultancy.education.DTOs.responseDTOs.lead.LeadPageResponseDto;
import com.consultancy.education.DTOs.responseDTOs.lead.LeadResponseDto;
import com.consultancy.education.DTOs.responseDTOs.lead.LeadStatusCountDto;

public interface LeadService {
    LeadResponseDto createLead(LeadRequestDto leadRequestDto, String createdByUserEmail);

    LeadPageResponseDto getLeads(LeadFilterDto filterDto);

    LeadResponseDto getLeadById(Long leadId);

    LeadResponseDto updateLead(Long leadId, UpdateLeadRequestDto updateLeadRequestDto, String updatedByUserEmail);

    Long countTotalLeads();

    LeadStatusCountDto getLeadStatusCounts();
}
