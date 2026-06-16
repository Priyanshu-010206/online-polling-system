package com.jforce.polling.controller;

import com.jforce.polling.model.Poll;
import com.jforce.polling.service.PollService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/polls")
public class PollRestController {

    @Autowired
    private PollService pollService;

    @PostMapping
    public ResponseEntity<Poll> createPoll(@RequestBody Map<String, Object> body) {
        String title = (String) body.get("title");
        String description = (String) body.get("description");
        @SuppressWarnings("unchecked")
        List<String> options = (List<String>) body.get("options");

        Poll created = pollService.createPoll(title, description, options);
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<Poll>> getAllPolls() {
        return ResponseEntity.ok(pollService.getAllPolls());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Poll> getPollById(@PathVariable Long id) {
        return pollService.getPollById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Poll> updatePoll(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Poll updated = pollService.updatePoll(id, body.get("title"), body.get("description"));
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePoll(@PathVariable Long id) {
        pollService.deletePoll(id);
        return ResponseEntity.ok("Poll deleted successfully");
    }

    @PostMapping("/{pollId}/vote/{optionId}")
    public ResponseEntity<Poll> vote(@PathVariable Long pollId, @PathVariable Long optionId) {
        Poll updated = pollService.vote(pollId, optionId);
        return ResponseEntity.ok(updated);
    }
}