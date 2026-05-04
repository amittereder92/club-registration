package edu.psu.ist.registration.service;

import edu.psu.ist.registration.dao.EventRepository;
import edu.psu.ist.registration.entity.Event;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventServiceImpl(EventRepository theEventRepository) {
        eventRepository = theEventRepository;
    }

    @Override
    public List<Event> findAll() {
        return eventRepository.findAllByOrderByEventDateDesc();
    }

    @Override
    public Event findById(int theId) {
        Optional<Event> result = eventRepository.findById(theId);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Event not found - " + theId);
        }
    }

    @Override
    @Transactional
    public Event save(Event theEvent) {
        return eventRepository.save(theEvent);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        eventRepository.deleteById(theId);
    }

    @Override
    public List<Event> findActiveEvents() {
        return eventRepository.findByIsActiveTrueOrderByEventDateAsc();
    }

    // Check current available event
    @Override
    public Optional<Event> findCurrentlyOpenEvent() {
        LocalDate today = LocalDate.now();

        return eventRepository.findAll()
                .stream()
                .filter(e -> e.getEventDate() != null)
                .filter(e -> !e.getEventDate().isBefore(today)) // event date is today or future
                .min(Comparator.comparing(Event::getEventDate)); // pick the nearest upcoming one
    }

    @Override
    public boolean isEventOpenForRegistration(int eventId) {
        Optional<Event> openEvent = findCurrentlyOpenEvent();
        return openEvent.isPresent() && openEvent.get().getEventId() == eventId;
    }
}
