package edu.psu.ist.registration.service;

import edu.psu.ist.registration.entity.Event;

import java.util.List;
import java.util.Optional;

public interface EventService {

    List<Event> findAll();

    Event findById(int theId);

    Event save(Event theEvent);

    void deleteById(int theId);

    List<Event> findActiveEvents();

    // Returns the single event currently open for registration
    Optional<Event> findCurrentlyOpenEvent();

    // Returns true if the given event is the one currently open
    boolean isEventOpenForRegistration(int eventId);
}
