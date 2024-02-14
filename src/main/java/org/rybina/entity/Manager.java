package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import java.util.List;

//@EqualsAndHashCode(callSuper = true)
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//@Entity
//@PrimaryKeyJoinColumn(name = "id")
public class Manager{

//    private String projectName;
//
//    @Builder
//    public Manager(Integer id, PersonalInfo personalInfo, String username, String info, Company company, Profile profile, List<UserChat> userChats, String projectName) {
//        super(id, personalInfo, username, info, company, profile, userChats);
//        this.projectName = projectName;
//    }
}
