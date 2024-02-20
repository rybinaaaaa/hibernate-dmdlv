package org.rybina.mapper;

public interface Mapper <F, T> {
    T mapFrom(F entity);
}
