package org.rybina.mapper;

import lombok.RequiredArgsConstructor;
import org.rybina.dto.UserReadDto;
import org.rybina.entity.User;

@RequiredArgsConstructor
public class UserReadMapper implements Mapper<User, UserReadDto> {

    private final CompanyReadMapper companyReadMapper;

    @Override
    public UserReadDto mapFrom(User entity) {
        return new UserReadDto(
                entity.getId(),
                entity.getPersonalInfo(),
                entity.getUsername(),
                companyReadMapper.mapFrom(entity.getCompany())
        );
    }
}
