package org.rybina.dto;

import org.rybina.entity.LocaleInfo;
import org.rybina.entity.PersonalInfo;

import java.util.Map;

public record UserReadDto(Integer id,
                          PersonalInfo personalInfo,
                          String username,
                          CompanyReadDto company) {
}
