package ro.msg.learning.feedback.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Sentiment {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int startIndex;
	private int endIndex;
	@Enumerated(EnumType.STRING)
	private SentimentType type;
	private boolean strong;
	private String text;
}
