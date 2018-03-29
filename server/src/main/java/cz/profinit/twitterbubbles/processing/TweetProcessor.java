package cz.profinit.twitterbubbles.processing;

import cz.profinit.twitterbubbles.model.TweetStats;
import lombok.extern.slf4j.Slf4j;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.reducing;

@Component
@Slf4j
public class TweetProcessor {

    private final AtomicInteger counter = new AtomicInteger(1);

    public TweetStats processTweet(Tweet tweet) {
        log.trace("Processing tweet number {}. Id: {}", counter.getAndIncrement(), tweet.getId());

        String text = tweet.getText();

        List<String> words = words(text);

        Map<String, Integer> wordCounts = countWords(words);

        return new TweetStats(text, wordCounts);
    }

    List<String> words(String text) {
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            words[i] = words[i].replaceAll("[^\\w]", "");
        }
        return Arrays.asList(words);
    }

    Map<String, Integer> countWords(List<String> words) {
        return words.stream().map(String::toLowerCase).collect(groupingBy(word -> word, reducing(0, e -> 1, Integer::sum)));
    }
}
