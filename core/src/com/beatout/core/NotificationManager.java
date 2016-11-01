package com.beatout.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {

    private Map<String, List<EventHandler>> observers = new HashMap<String, List<EventHandler>>();

    private static NotificationManager notificationManager;

    public <D> void addObserver(String id, EventHandler<D> target) {
        if (!observers.containsKey(id)) {
            observers.put(id, new ArrayList<EventHandler>());
        }

        observers.get(id).add(target);
    }

    public <D> void registerEvent(String id, D data) {
        Event<D> event = new Event<D>(data);
        List<EventHandler> handlerList = observers.get(id);
        if (handlerList != null) {
            try {
                for (EventHandler handler : handlerList) {
                    @SuppressWarnings("unchecked")
                    EventHandler<D> specificEventHandler = (EventHandler<D>) handler;
                    specificEventHandler.handleEvent(event);
                }
            } catch (ClassCastException e) {
                throw new NotificationException("Event handler of incorrect type called for event of type " + data.getClass().getName());
            }
        }
    }

    public <D> void clear() {
        observers.clear();
    }

    private final class NotificationException extends RuntimeException {
        public NotificationException(String message) {
            super(message);
        }
    }

    public final class Event<D>{
        public final D data;
        public Event(D data) {
            this.data = data;
        }
    }

    public interface EventHandler<T>{
        void handleEvent(Event<T> event);
    }

    public static NotificationManager getDefault(){
        if (notificationManager == null){
            notificationManager = new NotificationManager();
        }
        return notificationManager;
    }
}