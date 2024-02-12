package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@TypeDef(name = "convJson", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "users", schema = "public")
public class User {
    @Id
    private String username;

    private PersonalInfo personalInfo;

    @Type(type = "convJson")
    private String info;
}
