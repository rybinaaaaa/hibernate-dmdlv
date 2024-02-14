package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@NamedQuery(name = "findUserByName", query = "select  u from User u " +
                                             "join u.company c " +
                                             "where u.personalInfo.firstname = :firstname")
@Data
@ToString(exclude = {"profile", "userChats"})
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "username")
@TypeDef(name = "json", typeClass = JsonBinaryType.class)
@Entity
@Table(name = "users", schema = "public")
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Embedded
    private PersonalInfo personalInfo;

    @Column
    private String username;

    @Column(columnDefinition = "jsonb")
    @Type(type = "json")
    private String info;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Builder.Default
    @OneToMany(mappedBy = "user")
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "receiver")
    private List<Payment> payments = new ArrayList<>();

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastName();
    }
}
