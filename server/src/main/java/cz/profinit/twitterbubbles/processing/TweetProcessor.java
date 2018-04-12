package cz.profinit.twitterbubbles.processing;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader;
import cz.profinit.twitterbubbles.model.TweetStats;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Stream;

@Component
public class TweetProcessor {

    private static final Logger log = LoggerFactory.getLogger(TweetProcessor.class);

    public TweetStats processTweet(Tweet tweet) {
        log.trace("Processing tweet: {}", tweet.getText());

        String text = tweet.getText();

        // TODO Rozdělení textu do slov a spočítání počtu jejich výskytů.
        // TODO Implementace je hotová, pokud uspěje unit test TweeProcessorTest.

        Map<String, Integer> resultMap = new HashMap<>();
        Arrays.stream(text.toLowerCase().split(" ")).forEach(s->{
            List<Character> chrs = new ArrayList<>();
            for(char ch : s.toCharArray()){
                chrs.add(ch);
            }
            String filteredS = chrs.stream()
                    .filter(Character::isLetter)
                    .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
            Integer count = resultMap.get(filteredS);
            if (count == null){
                resultMap.put(filteredS, 1);
            }else{
                resultMap.put(filteredS, ++count);
            }
        });

        return new TweetStats(resultMap);
    }
}
