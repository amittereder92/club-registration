package edu.psu.ist.registration.dao;

import edu.psu.ist.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface RegistrationRepository extends JpaRepository<Registration, Integer> {

    // All registrations for a specific event
    List<Registration> findByEvent_EventIdOrderByRegistrationDateAsc(int eventId);

    // All events a member is registered for
    List<Registration> findByMember_MemberId(int memberId);

    // Check if a member is already registered for an event
    Optional<Registration> findByMember_MemberIdAndEvent_EventId(int memberId, int eventId);

    // Count registrations for an event
    int countByEvent_EventId(int eventId);
}
