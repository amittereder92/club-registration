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
        Member theMember = null;
        if (result.isPresent()) {
            theMember = result.get();
        } else {
            throw new RuntimeException("Member not found - " + theId);
        }
        return theMember;
    }
 
    @Override
    public Member save(Member theMember) {
        return memberRepository.save(theMember);
    }
 
    @Override
    public void deleteById(int theId) {
        memberRepository.deleteById(theId);
    }
}