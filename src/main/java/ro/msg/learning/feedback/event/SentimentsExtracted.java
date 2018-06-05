package ro.msg.learning.feedback.event;

import lombok.Data;
import ro.msg.learning.feedback.model.Feedback;

@Data
public class SentimentsExtracted {
	private final Feedback feedback;
}
