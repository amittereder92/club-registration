package edu.psu.ist.registration.controller;

import edu.psu.ist.registration.dao.RegistrationRepository;
import edu.psu.ist.registration.entity.Event;
import edu.psu.ist.registration.entity.Member;
import edu.psu.ist.registration.service.EventService;
import edu.psu.ist.registration.service.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/members")
public class MemberController {

    private MemberService memberService;
    private EventService eventService;
    private RegistrationRepository registrationRepository;

    @Autowired
    public MemberController(MemberService theMemberService,
                            EventService theEventService,
                            RegistrationRepository theRegistrationRepository) {
        memberService = theMemberService;
        eventService = theEventService;
        registrationRepository = theRegistrationRepository;
    }

    @GetMapping("/list")
    public String listMembers(Model theModel) {
        List<Member> theMembers = memberService.findAll();
        theModel.addAttribute("members", theMembers);
        return "members/list-members";
    }

    @GetMapping("/showFormForAdd")
    public String showFormForAdd(Model theModel) {
        Member theMember = new Member();
        theModel.addAttribute("member", theMember);
        return "members/member-form";
    }

    @GetMapping("/showFormForUpdate")
    public String showFormForUpdate(@RequestParam("memberId") int theId, Model theModel) {
        Member theMember = memberService.findById(theId);
        theModel.addAttribute("member", theMember);
        return "members/member-form";
    }

    @PostMapping("/save")
    public String saveMember(@ModelAttribute("member") Member theMember) {
        memberService.save(theMember);
        return "redirect:/members/list";
    }

    @GetMapping("/delete")
    public String delete(@RequestParam("memberId") int theId) {
        memberService.deleteById(theId);
        return "redirect:/members/list";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model theModel) {
        theModel.addAttribute("member", new Member());

        // Find the currently open event and its registration count
        Optional<Event> openEvent = eventService.findCurrentlyOpenEvent();
        if (openEvent.isPresent()) {
            Event event = openEvent.get();
            int registeredCount = registrationRepository.countByEvent_EventId(event.getEventId());
            int spotsRemaining = event.getCapacity() - registeredCount;

            theModel.addAttribute("openEvent", event);
            theModel.addAttribute("registeredCount", registeredCount);
            theModel.addAttribute("spotsRemaining", spotsRemaining);
            theModel.addAttribute("registrationOpen", true);
        } else {
            theModel.addAttribute("openEvent", null);
            theModel.addAttribute("registeredCount", 0);
            theModel.addAttribute("spotsRemaining", 0);
            theModel.addAttribute("registrationOpen", false);
        }

        return "vgd-registration";
    }
}
