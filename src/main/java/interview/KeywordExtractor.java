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

        for (String word : uniqueWords) {
            scores.put(word, tfidf.tfidf(word, document));
        }

        TreeMap<String, Double> sortedScoresMap = new TreeMap(new ValueComparator(scores));
        sortedScoresMap.putAll(scores);

        TreeMap<String, Double> topSortedScoresMap = new TreeMap(new ValueComparator(scores));
        while (topSortedScoresMap.size() < top) {
            Map.Entry<String, Double> entry = sortedScoresMap.pollFirstEntry();
            topSortedScoresMap.put(entry.getKey(), entry.getValue());
        }

        return topSortedScoresMap;
    }

    private static class ValueComparator implements Comparator<String> {
        Map<String, Double> map;

        public ValueComparator(Map<String, Double> map) {
            this.map = map;
        }

        @Override
        public int compare(String s1, String s2) {
            return -1 * Double.compare(map.get(s1), map.get(s2));
        }
    }
}
