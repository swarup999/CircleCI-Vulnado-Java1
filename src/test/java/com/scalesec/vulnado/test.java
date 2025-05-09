import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.List;
import static org.junit.Assert.*;

public class LinkListerTest {

    // Helper method to mock Jsoup connection and document
    private void mockJsoupConnection(String url, String htmlContent) throws IOException {
        Document mockDocument = Jsoup.parse(htmlContent);
        Mockito.when(Jsoup.connect(url).get()).thenReturn(mockDocument);
    }

    @Test
    public void getLinks_ShouldReturnLinks() throws IOException {
        String url = "http://example.com";
        String htmlContent = "<html><body><a href='http://example.com/link1'>Link1</a><a href='http://example.com/link2'>Link2</a></body></html>";
        mockJsoupConnection(url, htmlContent);

        List<String> links = LinkLister.getLinks(url);

        assertEquals("Expected two links", 2, links.size());
        assertTrue("Expected link1", links.contains("http://example.com/link1"));
        assertTrue("Expected link2", links.contains("http://example.com/link2"));
    }

    @Test(expected = IOException.class)
    public void getLinks_ShouldThrowIOException() throws IOException {
        String url = "http://invalid-url.com";
        Mockito.when(Jsoup.connect(url).get()).thenThrow(new IOException("Invalid URL"));

        LinkLister.getLinks(url);
    }

    @Test
    public void getLinksV2_ShouldReturnLinks() throws BadRequest, IOException {
        String url = "http://example.com";
        String htmlContent = "<html><body><a href='http://example.com/link1'>Link1</a><a href='http://example.com/link2'>Link2</a></body></html>";
        mockJsoupConnection(url, htmlContent);

        List<String> links = LinkLister.getLinksV2(url);

        assertEquals("Expected two links", 2, links.size());
        assertTrue("Expected link1", links.contains("http://example.com/link1"));
        assertTrue("Expected link2", links.contains("http://example.com/link2"));
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForPrivateIP() throws BadRequest {
        String url = "http://192.168.0.1";
        LinkLister.getLinksV2(url);
    }

    @Test(expected = BadRequest.class)
    public void getLinksV2_ShouldThrowBadRequestForInvalidURL() throws BadRequest {
        String url = "invalid-url";
        LinkLister.getLinksV2(url);
    }
}
