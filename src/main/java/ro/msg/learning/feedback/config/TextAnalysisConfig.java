package ro.msg.learning.feedback.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@EnableHystrix
@ConfigurationProperties(prefix = "ro.msg.learning.ta")
public class TextAnalysisConfig {
	private String url;
	private List<String> positive;
	private List<String> negative;
}
