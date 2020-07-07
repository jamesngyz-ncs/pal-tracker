package io.pivotal.pal.tracker;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    Map<Long, TimeEntry> idToTimeEntry = new HashMap<>();
    long id = 0;

    public TimeEntry create(TimeEntry timeEntry) {
        id ++;
        timeEntry.setId(id);
        idToTimeEntry.put(timeEntry.getId(), timeEntry);
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return idToTimeEntry.get(id);
    }

    public List<TimeEntry> list() {
        return new ArrayList<>(idToTimeEntry.values());
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        if (idToTimeEntry.containsKey(id)) {
            timeEntry.setId(id);
            idToTimeEntry.put(id, timeEntry);
            return idToTimeEntry.get(id);
        } else {
            return null;
        }
    }

    public void delete(long id) {
        idToTimeEntry.remove(id);
    }

}
