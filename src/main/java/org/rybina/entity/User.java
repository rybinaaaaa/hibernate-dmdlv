package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@ToString(exclude = {"profile", "userChats"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@TypeDef(name = "convJson", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "users", schema = "public")
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Embedded
    private PersonalInfo personalInfo;

    @Column
    private String username;

    @Type(type = "convJson")
    private String info;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();
}
