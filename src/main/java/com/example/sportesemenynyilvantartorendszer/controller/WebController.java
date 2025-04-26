package com.example.sportesemenynyilvantartorendszer.controller;

import com.example.sportesemenynyilvantartorendszer.model.Event;
import com.example.sportesemenynyilvantartorendszer.model.Participant;
import com.example.sportesemenynyilvantartorendszer.service.EventService;
import com.example.sportesemenynyilvantartorendszer.service.ParticipantService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final EventService eventService;
    private final ParticipantService participantService;

    @ModelAttribute("favoriteCount")
    public long favoriteCount() {
        return eventService.findAll().stream().filter(Event::isFavorite).count();
    }

    @ModelAttribute("categories")
    public List<String> categories() {
        return List.of("Futás", "Kosárlabda", "Tenisz", "Úszás", "Foci", "Röplabda", "Kézilabda", "Jégkorong", "Kerékpározás", "Vívás", "Asztalitenisz");
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findAll());
        model.addAttribute("title", "Összes sportesemény");
        return "index";
    }

    @GetMapping("/football")
    public String footballPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Foci"));
        model.addAttribute("title", "Foci események");
        return "event-list";
    }

    @GetMapping("/basketball")
    public String basketballPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Kosárlabda"));
        model.addAttribute("title", "Kosárlabda események");
        return "event-list";
    }

    @GetMapping("/tennis")
    public String tennisPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Tenisz"));
        model.addAttribute("title", "Tenisz események");
        return "event-list";
    }

    @GetMapping("/swimming")
    public String swimmingPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Úszás"));
        model.addAttribute("title", "Úszás események");
        return "event-list";
    }

    @GetMapping("/running")
    public String runningPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Futás"));
        model.addAttribute("title", "Futás események");
        return "event-list";
    }

    @GetMapping("/volleyball")
    public String volleyballPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Röplabda"));
        model.addAttribute("title", "Röplabda események");
        return "event-list";
    }

    @GetMapping("/handball")
    public String handballPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Kézilabda"));
        model.addAttribute("title", "Kézilabda események");
        return "event-list";
    }

    @GetMapping("/icehockey")
    public String icehockeyPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Jégkorong"));
        model.addAttribute("title", "Jégkorong események");
        return "event-list";
    }

    @GetMapping("/cycling")
    public String cyclingPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Kerékpározás"));
        model.addAttribute("title", "Kerékpár események");
        return "event-list";
    }

    @GetMapping("/fencing")
    public String fencingPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Vívás"));
        model.addAttribute("title", "Vívás események");
        return "event-list";
    }

    @GetMapping("/tabletennis")
    public String tabletennisPage(Model model) {
        model.addAttribute("events", eventService.findByKeyword("Asztalitenisz"));
        model.addAttribute("title", "Asztalitenisz események");
        return "event-list";
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model) {
        model.addAttribute("events", eventService.findAll().stream().filter(Event::isFavorite).toList());
        model.addAttribute("title", "Kedvenc események");
        return "event-list";
    }

    @GetMapping("/upcoming-events")
    public String upcomingPage(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null && event.getDate().isAfter(LocalDate.now()))
                .toList());
        model.addAttribute("title", "Közelgő események");
        return "event-list";
    }

    @GetMapping("/completed-events")
    public String completedPage(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null && !event.getDate().isAfter(LocalDate.now()))
                .toList());
        model.addAttribute("title", "Teljesített események");
        return "event-list";
    }

    @GetMapping("/add")
    public String showAddPage(Model model) {
        model.addAttribute("participant", new Participant());
        model.addAttribute("events", eventService.findAll());
        return "add";
    }

    @PostMapping("/add-participant")
    public String addParticipant(@ModelAttribute Participant participant,
                                 @RequestParam(required = false) String category,
                                 @RequestParam(required = false) String newEventName,
                                 @RequestParam(required = false) String newEventLocation,
                                 RedirectAttributes redirectAttributes) {

        if (participant.getAge() < 0 || participant.getAge() > 99) {
            redirectAttributes.addAttribute("error", "invalidAge");
            return "redirect:/add";
        }

        boolean existingSelected = participant.getEvent() != null && participant.getEvent().getId() != null;
        boolean newEventProvided = newEventName != null && !newEventName.isBlank()
                && newEventLocation != null && !newEventLocation.isBlank();

        if (!existingSelected && !newEventProvided) {
            redirectAttributes.addAttribute("error", "noEvent");
            return "redirect:/add";
        }

        if (newEventProvided && !existingSelected) {
            Event newEvent = Event.builder()
                    .name(newEventName)
                    .location(newEventLocation)
                    .category(category)
                    .date(participant.getActivityDate())
                    .favorite(false)
                    .build();
            eventService.save(newEvent);
            participant.setEvent(newEvent);
        } else if (existingSelected) {
            Event existingEvent = eventService.findById(participant.getEvent().getId()).orElse(null);
            if (existingEvent != null) {
                participant.setActivityDate(existingEvent.getDate());
                participant.setEvent(existingEvent);
            }
        }

        participantService.save(participant);
        return "redirect:/";
    }

    @PostMapping("/toggle-favorite/{id}")
    public String toggleFavorite(@PathVariable Long id, HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {
            event.setFavorite(!event.isFavorite());
            eventService.save(event);
        });
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/unfollow-favorite/{id}")
    public String unfollowFavorite(@PathVariable Long id, HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {
            if (event.isFavorite()) {
                event.setFavorite(false);
                eventService.save(event);
            }
        });
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }

    @PostMapping("/delete-participant/{id}")
    public String deleteParticipant(@PathVariable Long id, HttpServletRequest request) {
        participantService.deleteById(id);
        String referer = request.getHeader("Referer");
        return "redirect:" + (referer != null ? referer : "/");
    }
}
