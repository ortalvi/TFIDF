package interview;

import interview.fetcher.HttpFetcher;
import interview.fetcher.IDescriptionFetcher;
import interview.tfidf.TFIDF;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    private static final String FILE_NAME = "id.txt";
    private static final String URL_ADDRESS = "http://itunes.apple.com/lookup?id=";

    public static void main(String[] args) {
        Map<String, Map<String, Double>> keywordsPerApp = new HashMap<>();
        IDescriptionFetcher descriptionFetcher = new HttpFetcher(URL_ADDRESS);
        DocumentBuilder documentBuilder = new DocumentBuilder(descriptionFetcher);
        List<String> applicationIds = getApplicationIds();
        List<Document> documents = documentBuilder.getDocuments(applicationIds);
        TFIDF tfidf = new TFIDF(documents);
        KeywordExtractor keywordExtractor = new KeywordExtractor(tfidf);
        for (Document document : documents) {
            keywordsPerApp.put(document.getId(), keywordExtractor.extractKeywords(document, 10));
        }
        printResult(keywordsPerApp);
    }

    private static void printResult(Map<String, Map<String, Double>> keywordsPerApp) {
        StringBuilder stringBuilder = new StringBuilder();

        for (Map.Entry<String, Map<String, Double>> entry : keywordsPerApp.entrySet()) {
            stringBuilder.append(entry.getKey() + ": ");
            for (Map.Entry<String, Double> value : entry.getValue().entrySet()) {
                stringBuilder.append(value.getKey() + " " + value.getValue() + ", ");
            }
            stringBuilder.deleteCharAt(stringBuilder.length() - 1);
            stringBuilder.append("\n");
        }

        System.out.print(stringBuilder.toString());
    }

    private static List<String> getApplicationIds() {
        try {
            List<String> appIds = new ArrayList<>();
            Scanner scanner = new Scanner(new File(FILE_NAME));
            while (scanner.hasNextLine()) {
                appIds.add(scanner.nextLine());
            }
            return appIds;
        } catch (FileNotFoundException e) {
            return Collections.emptyList();
        }
    }
}
