package org.example.authservice.mapper;

import org.example.authservice.dto.response.CustomerRegistrationResponseDto;
import org.example.authservice.entity.Customer;
import org.example.authservice.entity.CustomerAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(source = "customer.id", target = "id")
    CustomerRegistrationResponseDto toResponseDto(Customer customer, CustomerAccount account);
}
