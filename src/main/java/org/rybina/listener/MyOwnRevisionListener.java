package org.rybina.listener;

import org.hibernate.envers.RevisionListener;
import org.rybina.entity.Revision;

public class MyOwnRevisionListener implements RevisionListener {

    @Override
    public void newRevision(Object revisionEntity) {
        ((Revision) revisionEntity).setUserInfo("yaaa");
    }
}
