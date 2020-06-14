package com.lab.labbook.ui.views.common;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;

public class MessagesView {
    public static final String OK = "200";
    public static final String NOT_OK = "100";
    public static final String DEFAULT_ERROR_MESSAGE = "Something went wrong! Check your data and try again!";

    public static boolean showException(String status, String message) {
        if (status.equals("200")) {
            return true;
        } else {
            Div content = new Div();
            content.addClassName("alert-style");
            content.setText(message);
            Notification notification = new Notification(content);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.setDuration(3000);
            notification.open();
            return false;
        }
    }
}
