package edu.psu.ist.registration.service;
 
import java.util.List;
 
import edu.psu.ist.registration.entity.Member;
 
public interface MemberService {
 
    List<Member> findAll();
 
    Member findById(int theId);
 
    Member save(Member theMember);
 
    void deleteById(int theId);
 
}