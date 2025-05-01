package com.example.sportesemenynyilvantartorendszer.controller.rest;

import com.example.sportesemenynyilvantartorendszer.exception.NoSuchEntityException;
import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
public class EventRestController {

    private final EventService eventService;

    @GetMapping
    public List<Event> getAllEvents() {
        return eventService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable Long id) {
        Event event = eventService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Nincs ilyen esemény ID: " + id));
        return ResponseEntity.ok(event);
    }

    @GetMapping("/category/{category}")
    public List<Event> getEventsByCategory(@PathVariable String category) {
        return eventService.findByCategory(category);
    }

    @PostMapping
    public Event createEvent(@RequestBody Event event) {
        return eventService.save(event);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable Long id, @RequestBody Event updatedEvent) {
        Event existing = eventService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Nincs ilyen esemény ID: " + id));
        updatedEvent.setId(id);
        return ResponseEntity.ok(eventService.save(updatedEvent));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        Event existing = eventService.findById(id)
                .orElseThrow(() -> new NoSuchEntityException("Nincs ilyen esemény ID: " + id));
        eventService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
