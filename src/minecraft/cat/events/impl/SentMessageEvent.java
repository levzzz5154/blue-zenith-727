package cat.events.impl;

import cat.events.Event;

public class SentMessageEvent extends Event {
    public String message;

    public SentMessageEvent(String message) {
        this.message = message;
    }
}