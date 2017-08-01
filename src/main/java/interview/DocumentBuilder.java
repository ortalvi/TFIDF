package interview;

import interview.fetcher.IDescriptionFetcher;

import java.util.ArrayList;
import java.util.List;

public class DocumentBuilder {

    private IDescriptionFetcher descriptionFetcher;

    public DocumentBuilder(IDescriptionFetcher descriptionFetcher) {
        this.descriptionFetcher = descriptionFetcher;
    }

    public List<Document> getDocuments(List<String> ids) {
        List<Document> documents = new ArrayList<>(ids.size());
        for (String id : ids) {
            documents.add(new Document(id, descriptionFetcher.fetch(id)));
        }
        return documents;
    }
}
