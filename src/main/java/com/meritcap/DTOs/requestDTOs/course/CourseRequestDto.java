package com.meritcap.DTOs.requestDTOs.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CourseRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    private String department;

    @NotNull(message = "Graduation level is required")
    private String graduationLevel;

    private String specialization;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        CourseRequestDto that = (CourseRequestDto) obj;
        return Objects.equals(name, that.name) && Objects.equals(department, that.department) && graduationLevel.equals(that.graduationLevel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, department, graduationLevel);
    }

}
