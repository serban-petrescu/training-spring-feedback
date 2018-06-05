package ro.msg.learning.feedback.repository;

import org.springframework.data.repository.Repository;
import ro.msg.learning.feedback.model.Feedback;

import java.util.List;

public interface FeedbackRepository extends Repository<Feedback, Integer> {
	List<Feedback> findAll();

	void save(Feedback feedback);
}
