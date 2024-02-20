package org.rybina.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
@NamedEntityGraph(
        name = "WithCompanyAndChat",
        attributeNodes = {
                @NamedAttributeNode("company"),
                @NamedAttributeNode(value = "userChats", subgraph = "chatskillallmen")
        },
        subgraphs = {
                @NamedSubgraph(name ="chatskillallmen", attributeNodes = @NamedAttributeNode("chat"))
        }
)
@NamedEntityGraph(
        name = "WithCompany",
        attributeNodes = {
                @NamedAttributeNode(value = "company", subgraph = "locales"),
        },
        subgraphs ={
                @NamedSubgraph(name = "locales", attributeNodes =  @NamedAttributeNode("localeInfos"))
        }
)
@Audited
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "users")
public class User extends BaseEntity<Integer> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Valid
    @Embedded
    private PersonalInfo personalInfo;

    @Column
    @NotNull
    private String username;

    @Column(columnDefinition = "jsonb")
    @Type(type = "json")
    private String info;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @NotAudited
    private Company company;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @NotAudited
    private Profile profile;

    @Builder.Default
//    @BatchSize(size = 5)
    @NotAudited
    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private List<UserChat> userChats = new ArrayList<>();

    @Builder.Default
    @NotAudited
//    @Fetch(FetchMode.SUBSELECT)
    @OneToMany(mappedBy = "receiver", fetch = FetchType.LAZY)
    private List<Payment> payments = new ArrayList<>();

    public String fullName() {
        return getPersonalInfo().getFirstname() + " " + getPersonalInfo().getLastName();
    }
}

