package edu.psu.ist.registration.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import edu.psu.ist.registration.dao.MemberRepository;
import edu.psu.ist.registration.entity.Member;

@Service
public class MemberServiceImpl implements MemberService {

    private MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository theMemberRepository) {
        memberRepository = theMemberRepository;
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAllByOrderByLastNameAsc();
    }

    @Override
    public Member findById(int theId) {
        Optional<Member> result = memberRepository.findById(theId);
        if (result.isPresent()) {
            return result.get();
        } else {
            throw new RuntimeException("Member not found - " + theId);
        }
    }

 
    @Override
    @Transactional
    public Member save(Member theMember) {
        // Only check duplicates for new members (memberId == 0)
        if (theMember.getMemberId() == 0
                && theMember.getEmail() != null
                && !theMember.getEmail().isBlank()) {

            Optional<Member> existing = memberRepository.findByEmail(theMember.getEmail().trim());

            if (existing.isPresent()) {
                Member existingMember = existing.get();

                // Preserve identity fields — does NOT overwrite these
                // existingMember.firstName stays
                // existingMember.lastName stays
                // existingMember.userName stays

                // Update everything else
                if (theMember.getPhoneNumber()     != null) existingMember.setPhoneNumber(theMember.getPhoneNumber());
                if (theMember.getBestGame()         != null) existingMember.setBestGame(theMember.getBestGame());
                if (theMember.getAddress()          != null) existingMember.setAddress(theMember.getAddress());
                if (theMember.getCity()             != null) existingMember.setCity(theMember.getCity());
                if (theMember.getState()            != null) existingMember.setState(theMember.getState());
                if (theMember.getZipCode()          != null) existingMember.setZipCode(theMember.getZipCode());
                if (theMember.getAgeRange()         != null) existingMember.setAgeRange(theMember.getAgeRange());
                if (theMember.getGuardianName()     != null) existingMember.setGuardianName(theMember.getGuardianName());
                if (theMember.getGuardianPhone()    != null) existingMember.setGuardianPhone(theMember.getGuardianPhone());
                if (theMember.getCollegeStudent()   != null) existingMember.setCollegeStudent(theMember.getCollegeStudent());
                if (theMember.getInterestedInPSU()  != null) existingMember.setInterestedInPSU(theMember.getInterestedInPSU());
                if (theMember.getGradeYear()        != null) existingMember.setGradeYear(theMember.getGradeYear());
                if (theMember.getGamingSystem()     != null) existingMember.setGamingSystem(theMember.getGamingSystem());
                if (theMember.getAttendedBefore()   != null) existingMember.setAttendedBefore(theMember.getAttendedBefore());
                if (theMember.getJoinTournament()   != null) existingMember.setJoinTournament(theMember.getJoinTournament());
                if (theMember.getHowHeard()         != null) existingMember.setHowHeard(theMember.getHowHeard());
                if (theMember.isAcceptedTerms())             existingMember.setAcceptedTerms(true);

                return memberRepository.save(existingMember);
            }
        }

        // No duplicate found — save normally
        return memberRepository.save(theMember);
    }

    @Override
    @Transactional
    public void deleteById(int theId) {
        memberRepository.deleteById(theId);
    }
}
