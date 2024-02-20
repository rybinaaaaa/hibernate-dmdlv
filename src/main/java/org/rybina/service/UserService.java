package org.rybina.service;

import lombok.RequiredArgsConstructor;
import org.hibernate.graph.GraphSemantic;
import org.rybina.dao.UserRepository;
import org.rybina.dto.UserCreateDto;
import org.rybina.dto.UserReadDto;
import org.rybina.entity.User;
import org.rybina.mapper.Mapper;
import org.rybina.mapper.UserCreateMapper;
import org.rybina.mapper.UserReadMapper;

import javax.transaction.Transactional;
import javax.validation.*;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserReadMapper userReadMapper;
    private final UserCreateMapper userCreateMapper;

    @Transactional
    public Integer create(UserCreateDto userCreateDto) {
        // validation

        ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory();
        Validator validator = validatorFactory.getValidator();
        Set<ConstraintViolation<UserCreateDto>> validated = validator.validate(userCreateDto);

        if (!validated.isEmpty()) {
            throw new ConstraintViolationException(validated);
        }

        // map
        User user = userCreateMapper.mapFrom(userCreateDto);
        return userRepository.save(user).getId();
    }

    @Transactional
    public Optional<UserReadDto> findById(Integer id, Mapper<User, UserReadDto> mapper) {
        Map<String, Object> properties = Map.of(GraphSemantic.LOAD.getJpaHintName(), userRepository.getEntityManager().getEntityGraph("WithCompany"));
        return userRepository.findById(id, properties).map(mapper::mapFrom);
    }

    @Transactional
    public Optional<UserReadDto> findById(Integer id) {
        return findById(id, userReadMapper);
    }

    @Transactional
    public boolean delete(Integer id) {
        Optional<User> maybeUser = userRepository.findById(id);
        maybeUser.ifPresent(user -> userRepository.delete(user.getId()));
        return maybeUser.isPresent();
    }
}
