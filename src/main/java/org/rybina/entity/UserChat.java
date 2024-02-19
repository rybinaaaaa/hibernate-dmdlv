package org.rybina.entity;

import lombok.*;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.rybina.listener.UserChatListener;
import org.hibernate.annotations.Cache;

import javax.persistence.*;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "users_chat")
@EntityListeners(UserChatListener.class)
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserChat extends AuditableEntity<Integer> {

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private Chat chat;

    public void setUser(User user) {
        this.user = user;
        user.getUserChats().add(this);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        this.chat.getUserChats().add(this);
    }
}
