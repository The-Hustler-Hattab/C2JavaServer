package com.mtattab.c2cServer.service;

import java.beans.PropertyChangeListener;

public interface ActiveSessionsObserver extends PropertyChangeListener {

    @Override
    void propertyChange(java.beans.PropertyChangeEvent evt);
}
