package org.rybina.mapper;

import lombok.RequiredArgsConstructor;
import org.rybina.dao.CompanyRepository;
import org.rybina.dto.UserCreateDto;
import org.rybina.entity.User;

@RequiredArgsConstructor
public class UserCreateMapper implements Mapper<UserCreateDto, User> {

    private final CompanyRepository companyRepository;

    @Override
    public User mapFrom(UserCreateDto entity) {
        return User.builder()
                .personalInfo(entity.personalInfo())
                .username(entity.username())
                .info(entity.info())
                .company(companyRepository.findById(entity.companyId()).orElse(null))
//                .company(companyRepository.findById(entity.companyId()).orElseThrow(IllegalArgumentException::new))
                .build();
    }
}
