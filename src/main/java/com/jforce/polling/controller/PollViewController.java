package com.jforce.polling.controller;

import com.jforce.polling.model.Poll;
import com.jforce.polling.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
public class PollViewController {

    @Autowired
    private PollService pollService;

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/polls/new")
    public String showCreateForm() {
        return "create-poll";
    }

    @PostMapping("/polls")
    public String createPoll(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String option1,
                             @RequestParam String option2) {
        List<String> options = Arrays.asList(option1, option2);
        pollService.createPoll(title, description, options);
        return "redirect:/polls";
    }

    @GetMapping("/polls")
    public String listPolls(Model model) {
        model.addAttribute("polls", pollService.getAllPolls());
        return "poll-list";
    }

    @GetMapping("/polls/{id}/vote")
    public String showVotePage(@PathVariable Long id, Model model) {
        Poll poll = pollService.getPollById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found"));
        model.addAttribute("poll", poll);
        return "vote";
    }

    @PostMapping("/polls/{id}/vote")
    public String castVote(@PathVariable Long id, @RequestParam Long optionId) {
        pollService.vote(id, optionId);
        return "redirect:/polls/" + id + "/results";
    }

    @GetMapping("/polls/{id}/results")
    public String showResults(@PathVariable Long id, Model model) {
        Poll poll = pollService.getPollById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found"));
        model.addAttribute("poll", poll);
        return "results";
    }
}