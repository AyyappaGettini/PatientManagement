package com.ty.patient_service.dto;

import com.ty.patient_service.validation_group.CreatePatientValidationGroup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PatientRequestDto {
    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name characters should not be more than 100")
    private String name;
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Date of Birth should not be blank")
    private String dateOfBirth;
    @NotNull(groups = CreatePatientValidationGroup.class, message = "Registered Date is required")
    private String registeredDate;
}
