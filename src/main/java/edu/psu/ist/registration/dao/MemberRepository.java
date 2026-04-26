package edu.psu.ist.registration.dao;
 
import java.util.List;
 
import org.springframework.data.jpa.repository.JpaRepository;
 
import edu.psu.ist.registration.entity.Member;
 
public interface MemberRepository extends JpaRepository<Member, Integer> {
 
    // sort members by last name ascending
    public List<Member> findAllByOrderByLastNameAsc();
 
}
 