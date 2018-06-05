package ro.msg.learning.feedback.event;

import lombok.Data;
import ro.msg.learning.feedback.model.Feedback;

@Data
public class FeedbackCreated {
	private final Feedback feedback;
}
