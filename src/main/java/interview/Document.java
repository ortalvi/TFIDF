package interview;

import java.util.Arrays;
import java.util.Collection;

public class Document {
    private String id;
    private Collection<String> words;

    public Document(String id, String description) {
        this.id = id;
        String[] words = description.split(" ");
        this.words = Arrays.asList(words);
    }

    public String getId() {
        return id;
    }

    public Collection<String> getWords() {
        return words;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Document document = (Document) o;

        return id.equals(document.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
