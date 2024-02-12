package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "convJson", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "users", schema = "public")
public class User {

    @EmbeddedId
    private PersonalInfo personalInfo;

    @Column
    private String username;


    @Type(type = "convJson")
    private String info;
}
