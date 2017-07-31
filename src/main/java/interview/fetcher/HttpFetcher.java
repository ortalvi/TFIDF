package interview.fetcher;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class HttpFetcher implements IDescriptionFetcher {
    private static final String DESCRIPTION_TAG = "description";

    public String fetch(String urlAddress) {
        try {
            URL url = new URL(urlAddress);
            URLConnection yc = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    yc.getInputStream()));

            StringBuffer stringBuffer = new StringBuffer();
            String inputLine = in.readLine();
            while (inputLine != null) {
                stringBuffer.append(stringBuffer);
                inputLine = in.readLine();
            }

            in.close();

            String description = getDescription(stringBuffer.toString());

            return description;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getDescription(String json) {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(json);
            JSONObject jsonObject = (JSONObject) obj;
            String description = (String) jsonObject.get(DESCRIPTION_TAG);

            return description;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
}
