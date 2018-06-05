package ro.msg.learning.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import ro.msg.learning.feedback.event.FeedbackCreated;
import ro.msg.learning.feedback.model.Feedback;
import ro.msg.learning.feedback.model.InboundFeedback;
import ro.msg.learning.feedback.repository.FeedbackRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {
	private final FeedbackRepository repository;
	private final SentimentService sentimentService;
	private final ApplicationEventPublisher publisher;

	public List<Feedback> readAll() {
		return repository.findAll();
	}

	public void createFeedback(InboundFeedback inbound) {
		Feedback feedback = toEntity(inbound);
		repository.save(feedback);
		publisher.publishEvent(new FeedbackCreated(feedback));
		sentimentService.createSentiments(feedback);
	}

	private static Feedback toEntity(InboundFeedback inbound) {
		Feedback feedback = new Feedback();
		feedback.setCreatedAt(LocalDateTime.now(ZoneOffset.UTC));
		feedback.setText(inbound.getText());
		return feedback;
	}
}
