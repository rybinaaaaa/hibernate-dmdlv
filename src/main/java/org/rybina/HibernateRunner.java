package org.rybina;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.graph.RootGraph;
import org.hibernate.graph.SubGraph;
import org.hibernate.jpa.QueryHints;
import org.rybina.entity.User;
import org.rybina.entity.UserChat;
import org.rybina.util.HibernateUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HibernateRunner {

    public static void main(String[] args) {
        List<User> users = new ArrayList<>();
        try (SessionFactory sessionFactory = HibernateUtil.buildSessionFactory();
             Session session = sessionFactory.openSession()) {
            session.getTransaction().begin();
//            RootGraph<?> graph = session.getEntityGraph("WithCompanyAndChat");

            RootGraph<User> userGraph = session.createEntityGraph(User.class);
            userGraph.addAttributeNodes("company", "userChats");
            SubGraph<UserChat> userChatSubGraph = userGraph.addSubgraph("userChats", UserChat.class);
            userChatSubGraph.addAttributeNodes("chat");


//            Hint providing a "loadgraph" EntityGraph. Attributes explicitly specified as AttributeNodes are treated as FetchType.EAGER (via join fetch or subsequent select). Attributes that are not specified are treated as FetchType.LAZY or FetchType.EAGER depending on the attribute's definition in metadata
            Map<String, Object> properties = Map.of(QueryHints.HINT_LOADGRAPH, userGraph);

//            Судя по документации щас нет отличий между fetch и load
//            Hint providing a "fetchgraph" EntityGraph. Attributes explicitly specified as AttributeNodes are treated as FetchType.EAGER (via join fetch or subsequent select). Note: Currently, attributes that are not specified are treated as FetchType.LAZY or FetchType.EAGER depending on the attribute's definition in metadata, rather than forcing FetchType.LAZY.
//            Map<String, Object> properties = Map.of(QueryHints.HINT_FETCHGRAPH, graph);

            var user = session.find(User.class, 1, properties);

            System.out.println(user.getCompany().getName());
        }
    }
}
