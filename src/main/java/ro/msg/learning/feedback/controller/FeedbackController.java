package ro.msg.learning.feedback.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import ro.msg.learning.feedback.event.FeedbackCreated;
import ro.msg.learning.feedback.event.SentimentsExtracted;
import ro.msg.learning.feedback.model.Feedback;
import ro.msg.learning.feedback.model.InboundFeedback;
import ro.msg.learning.feedback.service.FeedbackService;

import java.util.List;

@RestController
@RequestMapping("/rest")
@RequiredArgsConstructor
public class FeedbackController {
	private final FeedbackService service;
	private final SimpMessagingTemplate template;

	@GetMapping("/feedback")
	public List<Feedback> readAll() {
		return service.readAll();
	}


	@PostMapping("/feedback")
	public void createFeedback(@RequestBody InboundFeedback feedback) {
		service.createFeedback(feedback);
	}


	@EventListener
	public void onFeedbackCreated(FeedbackCreated event) {
		template.convertAndSend("/topic/feedback", event.getFeedback());
	}

	@EventListener
	public void onSentimentsExtracted(SentimentsExtracted event) {
		template.convertAndSend("/topic/sentiment", event.getFeedback());
	}
}
