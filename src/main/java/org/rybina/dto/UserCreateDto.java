package org.rybina.dto;

import org.rybina.entity.PersonalInfo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public record UserCreateDto(
        @Valid
        PersonalInfo personalInfo,
        @NotNull
        String username,
        String info,
        Integer companyId) {
}
