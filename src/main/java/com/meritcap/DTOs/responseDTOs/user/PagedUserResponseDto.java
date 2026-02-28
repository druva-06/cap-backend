package com.meritcap.DTOs.responseDTOs.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "Paginated user response DTO containing list of users and pagination metadata.")
public class PagedUserResponseDto {
    @Schema(description = "List of users for current page")
    List<UserResponseDto> users;

    @Schema(description = "Current page number (0-indexed)")
    int currentPage;

    @Schema(description = "Total number of pages")
    int totalPages;

    @Schema(description = "Total number of users")
    long totalElements;

    @Schema(description = "Number of items per page")
    int pageSize;

    @Schema(description = "Whether this is the first page")
    boolean first;

    @Schema(description = "Whether this is the last page")
    boolean last;
}
