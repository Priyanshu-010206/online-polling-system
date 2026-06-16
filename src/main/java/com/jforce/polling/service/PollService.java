package com.jforce.polling.service;

import com.jforce.polling.model.Option;
import com.jforce.polling.model.Poll;
import com.jforce.polling.repository.OptionRepository;
import com.jforce.polling.repository.PollRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PollService {

    @Autowired
    private PollRepository pollRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Poll createPoll(String title, String description, List<String> optionTexts) {
        Poll poll = new Poll();
        poll.setTitle(title);
        poll.setDescription(description);

        for (String text : optionTexts) {
            if (text != null && !text.trim().isEmpty()) {
                Option option = new Option();
                option.setText(text.trim());
                option.setVoteCount(0);
                poll.addOption(option);
            }
        }
        return pollRepository.save(poll);
    }

    public List<Poll> getAllPolls() {
        return pollRepository.findAll();
    }

    public Optional<Poll> getPollById(Long id) {
        return pollRepository.findById(id);
    }

    public Poll vote(Long pollId, Long optionId) {
        Poll poll = pollRepository.findById(pollId)
                .orElseThrow(() -> new RuntimeException("Poll not found with id: " + pollId));

        Option chosenOption = poll.getOptions().stream()
                .filter(o -> o.getId().equals(optionId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Option not found with id: " + optionId));

        chosenOption.setVoteCount(chosenOption.getVoteCount() + 1);
        optionRepository.save(chosenOption);

        return poll;
    }

    public Poll updatePoll(Long id, String title, String description) {
        Poll poll = pollRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Poll not found with id: " + id));
        poll.setTitle(title);
        poll.setDescription(description);
        return pollRepository.save(poll);
    }

    public void deletePoll(Long id) {
        pollRepository.deleteById(id);
    }
}