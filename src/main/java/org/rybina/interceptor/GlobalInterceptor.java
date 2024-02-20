package org.rybina.interceptor;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serial;
import java.io.Serializable;

public class GlobalInterceptor extends EmptyInterceptor {

    @Serial
    private static final long serialVersionUID = 8995472881574080246L;

    @Override
    public int[] findDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return super.findDirty(entity, id, currentState, previousState, propertyNames, types);
    }
}
