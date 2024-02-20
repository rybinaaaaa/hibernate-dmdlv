package org.rybina.dto;

import org.rybina.entity.PersonalInfo;

public record UserCreateDto(
        PersonalInfo personalInfo,
        String username,
        String info,
        Integer companyId) {
}
