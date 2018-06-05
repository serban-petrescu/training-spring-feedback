package ro.msg.learning.feedback.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ro.msg.learning.feedback.config.TextAnalysisConfig;
import ro.msg.learning.feedback.model.Sentiment;
import ro.msg.learning.feedback.model.SentimentType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SentimentExtractor {
	private final RestTemplate template = new RestTemplate();
	private final TextAnalysisConfig config;

	@HystrixCommand(fallbackMethod = "fallback")
	public List<Sentiment> extractFrom(String text) {
		log.info("Calling external service for '{}'.", text);
		Sentiment[] sentiments = template.getForObject(config.getUrl(),
				Sentiment[].class, text);
		return new LinkedList<>(Arrays.asList(sentiments));
	}

	private List<Sentiment> fallback(String text) {
		log.info("Falling back to local sentiment analysis for text '{}'.", text);
		String lowerText = text.toLowerCase();
		List<Sentiment> sentiments = new ArrayList<>();
		for (String word : config.getPositive()) {
			appendOccurrencesOf(sentiments, lowerText, word, SentimentType.POSITIVE);
		}
		for (String word : config.getNegative()) {
			appendOccurrencesOf(sentiments, lowerText, word, SentimentType.NEGATIVE);
		}
		return sentiments;
	}

	private void appendOccurrencesOf(List<Sentiment> target, String text, String word, SentimentType type) {
		int length = text.length() - word.length();
		for (int current = text.indexOf(word);
			 current >= 0 && current <= length; current = text.indexOf(word, current + 1)) {
			Sentiment sentiment = new Sentiment();
			sentiment.setType(type);
			sentiment.setStartIndex(current);
			sentiment.setEndIndex(current + word.length());
			sentiment.setText(word);
			sentiment.setType(type);
			sentiment.setStrong(false);
			target.add(sentiment);
		}
	}
}
