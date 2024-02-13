package org.rybina.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Profile {

    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne
    @PrimaryKeyJoinColumn
    private User user;

    private String street;

    private String language;

    public void setUser(User user) {
        user.setProfile(this);
//        user must have saved
        this.user = user;
        this.id = user.getId();
    }
}
