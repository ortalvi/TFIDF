package interview;

import interview.tfidf.TFIDF;

import java.util.*;

public class KeywordExtractor {

    private TFIDF tfidf;

    public KeywordExtractor(TFIDF tfidf) {
        this.tfidf = tfidf;
    }

    public Map<String, Double> extractKeywords(final Document document, int top) {
        Set<String> uniqueWords = new HashSet<>(document.getWords());
        Map<String, Double> scores = new HashMap<>(uniqueWords.size());
        Map<String, Double> keywords = new HashMap<>(top);
        for (String word : uniqueWords) {
            scores.put(word, tfidf.tfidf(word, document));
        }
        List<Map.Entry<String, Double>> entries = new ArrayList<>(scores.entrySet());
        Collections.sort(entries, new Comparator<Map.Entry<String, Double>>() {
            @Override
            public int compare(Map.Entry<String, Double> e1, Map.Entry<String, Double> e2) {
                return Double.compare(e1.getValue(), e2.getValue());
            }
        });
        for (Map.Entry<String, Double> entry : entries.subList(0, top)) {
            keywords.put(entry.getKey(), entry.getValue());
        }
        return keywords;
    }
}
