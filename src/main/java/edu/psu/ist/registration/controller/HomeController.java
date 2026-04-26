package edu.psu.ist.registration.controller;

import edu.psu.ist.registration.dao.EventRepository;
import edu.psu.ist.registration.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private MemberRepository memberRepository;
    private EventRepository eventRepository;

    @Autowired
    public HomeController(MemberRepository memberRepository, EventRepository eventRepository) {
        this.memberRepository = memberRepository;
        this.eventRepository = eventRepository;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalMembers", memberRepository.count());
        model.addAttribute("totalEvents",  eventRepository.count());
        model.addAttribute("activeEvents", eventRepository.findByIsActiveTrueOrderByEventDateAsc().size());
        return "dashboard";
    }
}
