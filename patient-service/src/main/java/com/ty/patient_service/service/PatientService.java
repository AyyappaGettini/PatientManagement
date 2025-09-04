package com.ty.patient_service.service;

import com.ty.patient_service.dto.PatientRequestDto;
import com.ty.patient_service.dto.PatientResponseDto;
import com.ty.patient_service.exception.EmailAddressAlreadyExists;
import com.ty.patient_service.exception.PatientNotFoundWithId;
import com.ty.patient_service.grpc.BillingServiceGrpcClient;
import com.ty.patient_service.kafka.KafkaProducer;
import com.ty.patient_service.mapper.PatientMapper;
import com.ty.patient_service.model.Patient;
import com.ty.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {
    private final PatientRepository patientRepository;
    private final BillingServiceGrpcClient billingServiceGrpcClient;
    private final KafkaProducer kafkaProducer;

    public List<PatientResponseDto> getPatients(){
        List<Patient> allPatients = patientRepository.findAll();
        return allPatients.stream().map(PatientMapper::toDto).toList();
    }

    public PatientResponseDto createPatient(PatientRequestDto patientRequestDto){
        if(patientRepository.existsByEmail(patientRequestDto.getEmail())){
            throw new EmailAddressAlreadyExists("Email Already Exists");
        }
        Patient newPatient = patientRepository.save(PatientMapper.toModel(patientRequestDto));
        billingServiceGrpcClient.createBillingAccount(newPatient.getId().toString(),newPatient.getName(),newPatient.getEmail());
        kafkaProducer.sendEvent(newPatient);
        return PatientMapper.toDto(newPatient);
    }

    public PatientResponseDto updatePatient(UUID id, PatientRequestDto patientRequestDto){
        Patient patient = patientRepository.findById(id).orElseThrow(()-> new PatientNotFoundWithId("Patient Not Found with these Id: "+ id));
        if(patientRepository.existsByEmailAndIdNot(patientRequestDto.getEmail(), id)){
            throw new EmailAddressAlreadyExists("Email Already Exists");
        }
        patient.setEmail(patientRequestDto.getEmail());
        patient.setAddress(patientRequestDto.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDto.getDateOfBirth()));
        patient.setName(patientRequestDto.getName());
        return PatientMapper.toDto(patient);
    }

    public void deletePatient(UUID id){
        patientRepository.deleteById(id);
    }



}
