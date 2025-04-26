package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/participants")
@RequiredArgsConstructor
public class ParticipantController {

    private final ParticipantService participantService;

    @GetMapping
    public List<Participant> getAllParticipants() {
        return participantService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Participant> getParticipantById(@PathVariable Long id) {
        return participantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Participant createParticipant(@RequestBody Participant participant) {
        return participantService.save(participant);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Participant> updateParticipant(@PathVariable Long id, @RequestBody Participant participantDetails) {
        return participantService.findById(id)
                .map(participant -> {
                    participant.setName(participantDetails.getName());
                    participant.setAge(participantDetails.getAge());
                    participant.setEmail(participantDetails.getEmail());
                    participant.setEvent(participantDetails.getEvent());
                    return ResponseEntity.ok(participantService.save(participant));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParticipant(@PathVariable Long id) {
        if (participantService.findById(id).isPresent()) {
            participantService.deleteById(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
