package interview.tfidf;

import interview.Document;

import java.util.*;

public class TFIDF {

    private int sizeOfCorpus;
    private Map<String, Integer> frequencyMap;
    private Map<Document, Map<String, Integer>> histogramMap;

    public TFIDF(Collection<Document> corpus) {
        sizeOfCorpus = corpus.size();
        histogramMap = getHistogramsMap(corpus);
        frequencyMap = getFrequencyMap(histogramMap);
    }

    /**
     * @param corpus
     * @return a map between every Document to the number of times word w appears in that document
     */
    private Map<Document, Map<String, Integer>> getHistogramsMap(Collection<Document> corpus) {
        Map<Document, Map<String, Integer>> histogramsMap = new HashMap<>();

        for (Document document : corpus) {
            Map<String, Integer> histogram = histogram(document.getWords());
            histogramsMap.put(document, histogram);
        }

        return histogramsMap;
    }

    /**
     * @param histograms
     * @return a map between each word (from all document in corpus) to the number of
     * documents with word w in it
     */
    private Map<String, Integer> getFrequencyMap(Map<Document, Map<String, Integer>> histograms) {
        Map<String, Integer> frequencyMap = new HashMap<>();

        for (Map<String, Integer> histogram : histograms.values()) {
            for (String word : histogram.keySet()) {
                int count = 0;
                if (frequencyMap.containsKey(word)) {
                    count = frequencyMap.get(word);
                }
                frequencyMap.put(word, count + 1);
            }
        }

        return frequencyMap;
    }

    double idf(String word) {
        int frequency = 0;
        if (frequencyMap.containsKey(word)) {
            frequency = frequencyMap.get(word);
        }

        return Math.log10(((double) sizeOfCorpus) / (1 + frequency));
    }

    double tf(String word, Document document) {
        int frequency = 0;
        if (histogramMap.containsKey(document)) {
            Map<String, Integer> histogram = histogramMap.get(document);

            if (histogram.containsKey(word)) {
                frequency = histogram.get(word);
            }
        }

        return ((double) frequency) / document.getWords().size();
    }

    public double tfidf(String word, Document document) {
        return tf(word, document) * idf(word);
    }

    private Map<String, Integer> histogram(Collection<String> words) {
        Map<String, Integer> histogram = new HashMap<>();
        for (String word : words) {
            int count = 0;
            if (histogram.containsKey(word)) {
                count = histogram.get(word);
            }

            histogram.put(word, count + 1);
        }

        return histogram;
    }
}
