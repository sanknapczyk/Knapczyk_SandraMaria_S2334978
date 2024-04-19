package com.gcu.knapczyk_sandramaria_s2334978;

import java.io.InputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObservationsParser {
    public List<Observations> parse(InputStream in) throws XmlPullParserException, IOException {
        List<Observations> observations = new ArrayList<>();
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();
        xpp.setInput(in, null);
        int eventType = xpp.getEventType();
        Observations currentObservation = null;

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                case XmlPullParser.START_TAG:
                    String localName = xpp.getName();
                    if ("item".equalsIgnoreCase(localName)) {
                        currentObservation = new Observations(); // Initialize a new instance
                    } else if (currentObservation != null) {
                        parseTag(xpp, currentObservation);
                    }
                    break;
                case XmlPullParser.END_TAG:
                    if ("item".equalsIgnoreCase(xpp.getName()) && currentObservation != null) {
                        observations.add(currentObservation);
                        currentObservation = null;
                    }
                    break;
            }
            eventType = xpp.next();
        }
        return observations;
    }

    private void parseTag(XmlPullParser xpp, Observations observation) throws IOException, XmlPullParserException {
        String localName = xpp.getName();
        if ("title".equalsIgnoreCase(localName)) {
            parseTitle(xpp, observation);
        } else if ("description".equalsIgnoreCase(localName)) {
            parseDescription(xpp, observation);
        } else if ("date".equalsIgnoreCase(localName)) {
            parseDate(xpp, observation);
        }
    }

    private void parseTitle(XmlPullParser xpp, Observations observation) throws IOException, XmlPullParserException {
        String titleText = xpp.nextText();
        String day = titleText.split(" -")[0];
        observation.setDay(day);
    }

    private void parseDescription(XmlPullParser xpp, Observations observation) throws IOException, XmlPullParserException {
        String descText = xpp.nextText();
        parseTemperature(descText, observation);
        parsePressure(descText, observation);
        parseWindSpeed(descText, observation);
    }

    private void parseDate(XmlPullParser xpp, Observations observation) throws IOException, XmlPullParserException {
        String dateText = xpp.nextText().split("T")[0];
        observation.setDate(dateText);
    }

    private void parseTemperature(String desc, Observations observation) {
        Pattern pattern = Pattern.compile("Temperature: (\\d+)(Â°C|°C)");
        Matcher matcher = pattern.matcher(desc);
        if (matcher.find()) {
            observation.setTemperature(matcher.group(1) + "°C");
        } else {
            observation.setTemperature("N/A");
        }
    }

    private void parsePressure(String desc, Observations observation) {
        Pattern pattern = Pattern.compile("Pressure: (\\d+mb)");
        Matcher matcher = pattern.matcher(desc);
        if (matcher.find()) {
            observation.setPressure(matcher.group(1));
        } else {
            observation.setPressure("N/A");
        }
    }

    private void parseWindSpeed(String desc, Observations observation) {
        Pattern pattern = Pattern.compile("Wind Speed: (\\d+mph)");
        Matcher matcher = pattern.matcher(desc);
        if (matcher.find()) {
            observation.setWindSpeed(matcher.group(1));
        } else {
            observation.setWindSpeed("N/A");
        }
    }
}
