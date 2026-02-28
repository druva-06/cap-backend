package com.meritcap.DTOs.responseDTOs.invitation;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class PagedInvitationResponseDto {
    List<InvitationResponseDto> invitations;
    int currentPage;
    int totalPages;
    long totalItems;
    int pageSize;
    boolean hasNext;
    boolean hasPrevious;
}
