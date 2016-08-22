package com.squalala.tariki.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by Fayçal KADDOURI on 14/08/16.
 */

@Root(name = "rowset")
public class Rowset {

    @ElementList(inline=true)
    private List<Event> events;

    public List<Event> getEvents() {
        return events;
    }

}
