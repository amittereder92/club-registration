package edu.psu.ist.registration.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import edu.psu.ist.registration.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Integer> {

    // Sort members by last name ascending
    public List<Member> findAllByOrderByLastNameAsc();

    // Find by email for duplicate checking
    public Optional<Member> findByEmail(String email);
}
