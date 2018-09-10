package com.sonluo.spongebob.spring.server;

import java.util.LinkedList;
import java.util.List;

/**
 * @author sunqian
 */
public abstract class AbstractSession implements Session {

    protected List<Runnable> destroyActions = null;

    @Override
    public void onDestroy(Runnable action) {
        if (destroyActions == null) {
            destroyActions = new LinkedList<>();
        }
        destroyActions.add(action);
    }
}
