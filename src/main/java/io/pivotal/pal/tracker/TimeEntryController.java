package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
public class TimeEntryController {

    private final TimeEntryRepository repository;
    private final DistributionSummary timeEntrySummary;
    private final Counter actionCounter;

    public TimeEntryController(
            TimeEntryRepository repository,
            MeterRegistry meterRegistry
    ) {
        this.repository = repository;

        this.timeEntrySummary = meterRegistry.summary("timeEntry.summary");
        this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry timeEntry) {
        TimeEntry created = repository.create(timeEntry);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());

        return ResponseEntity.created(URI.create(String.valueOf(created.getId()))).body(created);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable("id") Long id) {
        TimeEntry found = repository.find(id);
        if (found != null) {
            actionCounter.increment();
            return ResponseEntity.ok(found);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> list = repository.list();
        actionCounter.increment();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> update(@PathVariable("id") Long id,
                                            @RequestBody TimeEntry timeEntry) {
        TimeEntry updated = repository.update(id, timeEntry);

        if (updated != null) {
            actionCounter.increment();
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        repository.delete(id);
        actionCounter.increment();
        timeEntrySummary.record(repository.list().size());

        return ResponseEntity.noContent().build();
    }

}
