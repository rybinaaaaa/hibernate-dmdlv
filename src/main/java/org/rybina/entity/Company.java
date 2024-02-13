package org.rybina.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString(exclude = "users")
@AllArgsConstructor
@Builder
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Builder.Default
    @ElementCollection
    @CollectionTable(name = "company_locale", joinColumns = @JoinColumn(name = "company_id"))
//    @AttributeOverride(name = "название внуьри прикрепленного класса", column = @Column(name = "название колонки в бд"))
    private List<LocaleInfo> localeInfos = new ArrayList<>();

    @Builder.Default // написано для того, чтобы при нашем билдере ставилось дефолтное значение
//  orphanRemoval  — Что делать с таблицей родителем если мы из него удаляем какой-то дочерний элемент?
    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("username DESC, personalInfo.lastName ASC")
    private List<User> users = new ArrayList<>();

    public void addUser(User user) {
        users.add(user);
        user.setCompany(this);
    }
}
