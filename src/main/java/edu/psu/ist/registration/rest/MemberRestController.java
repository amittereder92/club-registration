package edu.psu.ist.registration.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.psu.ist.registration.entity.Member;
import edu.psu.ist.registration.service.MemberService;

@RestController
@RequestMapping("/api")
public class MemberRestController {

    private MemberService memberService;

    @Autowired
    public MemberRestController(MemberService theMemberService) {
        memberService = theMemberService;
    }

    // expose "/members" and return a list of members
    @GetMapping("/members")
    public List<Member> findAll() {
        return memberService.findAll();
    }

    // GET "/members/{memberId}"
    @GetMapping("/members/{memberId}")
    public Member getMember(@PathVariable int memberId) {

        Member theMember = memberService.findById(memberId);

        if (theMember == null) {
            throw new RuntimeException("Member id not found - " + memberId);
        }

        return theMember;
    }

    // POST /members - add new member
    @PostMapping("/members")
    public Member addMember(@RequestBody Member theMember) {

        theMember.setMemberId(0);

        Member dbMember = memberService.save(theMember);

        return dbMember;
    }

    // PUT /members - update existing member
    @PutMapping("/members")
    public Member updateMember(@RequestBody Member theMember) {

        Member dbMember = memberService.save(theMember);

        return dbMember;
    }

    // DELETE /members/{memberId}
    @DeleteMapping("/members/{memberId}")
    public String deleteMember(@PathVariable int memberId) {

        Member tempMember = memberService.findById(memberId);

        if (tempMember == null) {
            throw new RuntimeException("Member id not found = " + memberId);
        }

        memberService.deleteById(memberId);

        return "Deleted member id - " + memberId;
    }
}
