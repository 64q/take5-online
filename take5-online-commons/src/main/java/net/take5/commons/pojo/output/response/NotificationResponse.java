package net.take5.commons.pojo.output.response;

import net.take5.commons.pojo.output.AbstractResponse;

public class NotificationResponse extends AbstractResponse
{
    /** Message fourni */
    private String notification;

    public String getNotification()
    {
        return this.notification;
    }

    public void setNotification(String notification)
    {
        this.notification = notification;
    }

}
