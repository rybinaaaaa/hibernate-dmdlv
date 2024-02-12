package org.rybina.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.rybina.convertor.BirthdayConvertor;

import javax.persistence.Convert;
import javax.persistence.Embeddable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Embeddable
public class PersonalInfo {

    private String firstname;
    private String lastName;

    @Convert(converter = BirthdayConvertor.class)
    private Birthday birthday;
}
