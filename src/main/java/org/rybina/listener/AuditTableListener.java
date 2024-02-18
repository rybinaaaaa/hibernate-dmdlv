package org.rybina.listener;

import org.hibernate.event.spi.PreDeleteEvent;
import org.hibernate.event.spi.PreDeleteEventListener;
import org.hibernate.event.spi.PreInsertEvent;
import org.hibernate.event.spi.PreInsertEventListener;
import org.rybina.entity.Audit;

import java.io.Serial;

public class AuditTableListener implements PreDeleteEventListener, PreInsertEventListener {

    @Serial
    private static final long serialVersionUID = 6031724244654156176L;

    @Override
    public boolean onPreDelete(PreDeleteEvent event) {
        if ((event.getEntity() instanceof Audit)) {
            return false;
        }


            var audit = Audit.builder()
                .entityId(event.getId())
                .entityName(event.getEntityName())
//                .entityContent(event.getEntity().toString())
                .operation(Audit.Operation.DELETE)
                .build();

        event.getSession().save(audit);

        return false;
    }

    @Override
    public boolean onPreInsert(PreInsertEvent event) {
        if ((event.getEntity() instanceof Audit)) {
            return false;
        }

        var audit = Audit.builder()
                .entityId(event.getId())
                .entityName(event.getEntityName())
//                .entityContent(event.getEntity().toString())
                .operation(Audit.Operation.INSERT)
                .build();

        event.getSession().save(audit);

        return false;
    }
}
