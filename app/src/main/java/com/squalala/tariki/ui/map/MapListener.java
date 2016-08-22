package com.squalala.tariki.ui.map;

import com.squalala.tariki.models.Event;

import java.util.List;

/**
 * Created by Fayçal KADDOURI on 13/08/16.
 */
public interface MapListener {

    void onLoadEvents(List<Event> events);

}
