package org.rybina.mapper;

import org.rybina.dto.CompanyReadDto;
import org.rybina.entity.Company;

public class CompanyReadMapper implements Mapper<Company, CompanyReadDto> {

    @Override
    public CompanyReadDto mapFrom(Company entity) {
        return new CompanyReadDto(
                entity.getId(),
                entity.getName(),
                entity.getLocaleInfos());
    }
}
