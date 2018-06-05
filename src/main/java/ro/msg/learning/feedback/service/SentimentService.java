package ro.msg.learning.feedback.service;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ro.msg.learning.feedback.event.SentimentsExtracted;
import ro.msg.learning.feedback.model.Feedback;
import ro.msg.learning.feedback.repository.FeedbackRepository;

@Service
@RequiredArgsConstructor
public class SentimentService {
	private final SentimentExtractor sentimentExtractor;
	private final FeedbackRepository feedbackRepository;
	private final ApplicationEventPublisher publisher;

	@Async
	public void createSentiments(Feedback feedback) {
		feedback.setSentiments(sentimentExtractor.extractFrom(feedback.getText()));
		feedbackRepository.save(feedback);
		publisher.publishEvent(new SentimentsExtracted(feedback));
	}
}
