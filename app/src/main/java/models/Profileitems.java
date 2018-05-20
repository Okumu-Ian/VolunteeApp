package models;

import java.io.Serializable;

/**
 * Created by The Architect on 5/20/2018.
 */

public class Profileitems implements Serializable
{

    private String id;
    private int smallIcon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }
}
