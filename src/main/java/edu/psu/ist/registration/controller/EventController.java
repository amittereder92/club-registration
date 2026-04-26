package edu.psu.ist.registration.controller;

import edu.psu.ist.registration.dao.RegistrationRepository;
import edu.psu.ist.registration.entity.Event;
import edu.psu.ist.registration.entity.Registration;
import edu.psu.ist.registration.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/events")
public class EventController {

    private EventService eventService;
    private RegistrationRepository registrationRepository;

    @Autowired
    public EventController(EventService theEventService,
                           RegistrationRepository theRegistrationRepository) {
        eventService = theEventService;
        registrationRepository = theRegistrationRepository;
    }

    @GetMapping("/list")
    public String listEvents(Model theModel) {
        List<Event> theEvents = eventService.findAll();
        Optional<Event> openEvent = eventService.findCurrentlyOpenEvent();

        theModel.addAttribute("events", theEvents);
        theModel.addAttribute("openEventId", openEvent.map(Event::getEventId).orElse(-1));
        return "events/list-events";
    }

    @GetMapping("/view")
    public String viewEvent(@RequestParam("eventId") int theId, Model theModel) {
        Event theEvent = eventService.findById(theId);
        List<Registration> registrations =
                registrationRepository.findByEvent_EventIdOrderByRegistrationDateAsc(theId);

        Optional<Event> openEvent = eventService.findCurrentlyOpenEvent();
        boolean isOpen = openEvent.isPresent() && openEvent.get().getEventId() == theId;

        theModel.addAttribute("event", theEvent);
        theModel.addAttribute("registrations", registrations);
        theModel.addAttribute("registeredCount", registrations.size());
        theModel.addAttribute("spotsRemaining", theEvent.getCapacity() - registrations.size());
        theModel.addAttribute("isOpen", isOpen);
        return "events/view-event";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        theModel.addAttribute("event", new Event());
        return "events/event-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("eventId") int theId, Model theModel) {
        Event theEvent = eventService.findById(theId);
        theModel.addAttribute("event", theEvent);
        return "events/event-form";
    }

    @PostMapping("/save")
    public String saveEvent(@ModelAttribute("event") Event theEvent) {
        eventService.save(theEvent);
        return "redirect:/events/list";
    }

    @GetMapping("/delete")
    public String deleteEvent(@RequestParam("eventId") int theId) {
        eventService.deleteById(theId);
        return "redirect:/events/list";
    }
}
