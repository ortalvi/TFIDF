package interview;

import interview.fetcher.HttpFetcher;
import interview.fetcher.IDescriptionFetcher;
import interview.tfidf.TFIDF;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class Main {
    private static String FILE_NAME = "id.txt";
    private static String URL_ADDRESS = "http://itunes.apple.com/lookup?id=";

    public static void main(String args[]) {
        Map<String, Map<String, Double>> topTFIDFWordsMap = new HashMap<String, Map<String, Double>>();

        HttpFetcher httpFetcher = new HttpFetcher();
        List<Document> documentsList = getDocumentsList(httpFetcher);
        TFIDF tfidf = new TFIDF(documentsList);

        for (Document doc : documentsList) {
            Map<String, Double> algoMap = new HashMap<String, Double>();
            Set<String> words = new HashSet<String>(doc.getWords());
            for (String word : words) {
                algoMap.put(word, tfidf.tf(word, doc) * tfidf.idf(word));
            }

            SortedMap<String, Double> sortedMap = new TreeMap<String, Double>(algoMap);
            topTFIDFWordsMap.put(doc.getId(), getFirstEntries(10, sortedMap));
        }

        printResult(topTFIDFWordsMap);
    }

    private static void printResult(Map<String, Map<String, Double>> topTFIDFWordsMap) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Map<String, Double>> entry : topTFIDFWordsMap.entrySet()) {
            stringBuilder.append(entry.getKey() + ": ");
            for (Map.Entry<String, Double> value : entry.getValue().entrySet()) {
                stringBuilder.append(value.getKey() + " " + value.getValue() + ", ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }

        System.out.print(stringBuilder.toString());
    }

    public static SortedMap<String, Double> getFirstEntries(int num, SortedMap<String, Double> sortedMap) {
        int count = 0;

        TreeMap<String, Double> temp = new TreeMap<String, Double>();

        for (Map.Entry<String, Double> entry : sortedMap.entrySet()) {
            if (count >= num) break;

            temp.put(entry.getKey(), entry.getValue());
            count++;
        }

        return temp;
    }


    private static List<Document> getDocumentsList(IDescriptionFetcher descriptionFetcher) {
        List<Document> documentsList = new ArrayList<Document>();
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream(FILE_NAME);
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String id = sc.nextLine();
                documentsList.add(getDocument(id, descriptionFetcher, URL_ADDRESS + id));
            }

            if (sc.ioException() != null) {
                throw sc.ioException();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (sc != null) {
                sc.close();
            }
        }

        return documentsList;
    }

    /**
     * Receives fetcher, address and id and returns new Document
     *
     * @param id
     * @param fetcher
     * @param address
     * @return
     */

    private static Document getDocument(String id, IDescriptionFetcher fetcher, String address) {
        String description = fetcher.fetch(address);
        return new Document(id, description);
    }
}
