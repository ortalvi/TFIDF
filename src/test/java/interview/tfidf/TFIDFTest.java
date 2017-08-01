package interview.tfidf;

import interview.Document;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.*;

public class TFIDFTest {

    private static TFIDF tfidf;
    private static List<Document> documents;

    @BeforeClass
    public static void before() {
        documents = createDocumentList();
        tfidf = new TFIDF(documents);
    }

    private static List<Document> createDocumentList() {
        Document document1 = new Document("1", "this is a a sample");
        Document document2 = new Document("2", "this is another another example example example");
        return Arrays.asList(document1, document2);
    }

    @Test
    public void testTF() {
        // this
        Assert.assertEquals((double) 1 / 5, tfidf.tf("this", documents.get(0)), 0);
        Assert.assertEquals((double) 1 / 7, tfidf.tf("this", documents.get(1)), 0);

        // example
        Assert.assertEquals((double) 0 / 5, tfidf.tf("example", documents.get(0)), 0);
        Assert.assertEquals((double) 3 / 7, tfidf.tf("example", documents.get(1)), 0);
    }

    @Test
    public void testIDF() {
        // this
        Assert.assertEquals(Math.log10(2.0 / 3), tfidf.idf("this"), 0);

        // example
        Assert.assertEquals(Math.log10(2.0 / 2), tfidf.idf("example"), 0);
    }

    @Test
    public void testTFIDF() {
        // this
        Assert.assertEquals(((double) 1 / 5) * Math.log10(2.0 / 3), tfidf.tfidf("this", documents.get(0)), 0);
        Assert.assertEquals(((double) 1 / 7) * Math.log10(2.0 / 3), tfidf.tfidf("this", documents.get(1)), 0);

        // example
        Assert.assertEquals(((double) 0 / 5) * Math.log10(2.0 / 2), tfidf.tfidf("example", documents.get(0)), 0);
        Assert.assertEquals(((double) 3 / 7) * Math.log10(2.0 / 2), tfidf.tfidf("example", documents.get(1)), 0);
    }
}
