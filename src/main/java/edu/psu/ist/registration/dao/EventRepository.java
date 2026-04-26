package edu.psu.ist.registration.dao;

import edu.psu.ist.registration.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {

    // Find all active events
    List<Event> findByIsActiveTrueOrderByEventDateAsc();

    // Find all events ordered by date
    List<Event> findAllByOrderByEventDateDesc();
}
