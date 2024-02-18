package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
//@NamedEntityGraph(
//        name = "WithCompanyAndChat",
//        attributeNodes = {
//                @NamedAttributeNode("company"),
//                @NamedAttributeNode(value = "userChats", subgraph = "chatskillallmen")
//        },
//        subgraphs = {
//                @NamedSubgraph(name ="chatskillallmen", attributeNodes = @NamedAttributeNode("chat"))
//        }
//)
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profile profile;

    @Builder.Default
    @BatchSize(size = 5)
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastName();
    }
}

