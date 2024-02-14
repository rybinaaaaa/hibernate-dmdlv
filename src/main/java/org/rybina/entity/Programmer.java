package org.rybina.entity;

import lombok.*;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("p")
public class Programmer extends User {

    private Language language;

    @Builder
    public Programmer(Integer id, PersonalInfo personalInfo, String username, String info, Company company, Profile profile, List<UserChat> userChats, Language language) {
        super(id, personalInfo, username, info, company, profile, userChats);
        this.language = language;
    }
}
