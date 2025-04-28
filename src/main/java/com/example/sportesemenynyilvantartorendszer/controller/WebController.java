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
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebController {

    private final EventService eventService;
    private final ParticipantService participantService;

    @ModelAttribute("favoriteCount")
    public long favoriteCount() {
        return eventService.findAll().stream()
                .filter(Event::isFavorite)
                .count();
    }

    @ModelAttribute("categories")
    public List<String> categories() {
        return List.of("Futás", "Kosárlabda", "Tenisz", "Úszás", "Labdarúgás", "Röplabda",
                "Kézilabda", "Jégkorong", "Kerékpározás", "Vívás", "Asztalitenisz");
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Összes sportesemény");
        return "index";
    }

    @GetMapping("/football")
    public String footballPage(Model model) {
        return listEventsByCategory(model, "Labdarúgás", "Labdarúgás események");
    }

    @GetMapping("/basketball")
    public String basketballPage(Model model) {
        return listEventsByCategory(model, "Kosárlabda", "Kosárlabda események");
    }

    @GetMapping("/tennis")
    public String tennisPage(Model model) {
        return listEventsByCategory(model, "Tenisz", "Tenisz események");
    }

    @GetMapping("/swimming")
    public String swimmingPage(Model model) {
        return listEventsByCategory(model, "Úszás", "Úszás események");
    }

    @GetMapping("/running")
    public String runningPage(Model model) {
        return listEventsByCategory(model, "Futás", "Futás események");
    }

    @GetMapping("/volleyball")
    public String volleyballPage(Model model) {
        return listEventsByCategory(model, "Röplabda", "Röplabda események");
    }

    @GetMapping("/handball")
    public String handballPage(Model model) {
        return listEventsByCategory(model, "Kézilabda", "Kézilabda események");
    }

    @GetMapping("/icehockey")
    public String icehockeyPage(Model model) {
        return listEventsByCategory(model, "Jégkorong", "Jégkorong események");
    }

    @GetMapping("/cycling")
    public String cyclingPage(Model model) {
        return listEventsByCategory(model, "Kerékpározás", "Kerékpározás események");
    }

    @GetMapping("/fencing")
    public String fencingPage(Model model) {
        return listEventsByCategory(model, "Vívás", "Vívás események");
    }

    @GetMapping("/tabletennis")
    public String tabletennisPage(Model model) {
        return listEventsByCategory(model, "Asztalitenisz", "Asztalitenisz események");
    }

    @GetMapping("/favorites")
    public String favoritesPage(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(Event::isFavorite)
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Kedvenc események");
        return "event-list";
    }

    @GetMapping("/upcoming-events")
    public String upcomingEvents(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null && event.getDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Közelgő események");
        return "event-list";
    }

    @GetMapping("/completed-events")
    public String completedEvents(Model model) {
        model.addAttribute("events", eventService.findAll().stream()
                .filter(event -> event.getDate() != null && !event.getDate().isAfter(LocalDate.now()))
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", "Teljesített események");
        return "event-list";
    }

    @GetMapping("/add")
    public String showAddPage(Model model) {
        model.addAttribute("participant", new Participant());
        model.addAttribute("events", eventService.findAll().stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
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
        return "redirect:" + getReferer(request);
    }

    @PostMapping("/unfollow-favorite/{id}")
    public String unfollowFavorite(@PathVariable Long id, HttpServletRequest request) {
        eventService.findById(id).ifPresent(event -> {
            if (event.isFavorite()) {
                event.setFavorite(false);
                eventService.save(event);
            }
        });
        return "redirect:" + getReferer(request);
    }

    @PostMapping("/delete-participant/{id}")
    public String deleteParticipant(@PathVariable Long id, HttpServletRequest request) {
        participantService.deleteById(id);
        return "redirect:" + getReferer(request);
    }

    private String listEventsByCategory(Model model, String category, String title) {
        model.addAttribute("events", eventService.findByCategory(category).stream()
                .sorted(Comparator.comparing(Event::getDate))
                .toList());
        model.addAttribute("title", title);
        return "event-list";
    }


    private String getReferer(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return referer != null ? referer : "/";
    }
}
