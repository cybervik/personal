package com.whereyoudey.service;

import com.whereyoudey.service.Result;
import java.io.IOException;
import java.rmi.RemoteException;

import com.whereyoudey.webservice.ArrayOfString;
import com.whereyoudey.webservice.Search_Stub;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class SearchService {

    public static final int MAX_RESULTS = 10;

    public Result [] search(String name, String location, ArrayOfString filter) {
        Search_Stub service = new Search_Stub();
//		service._setProperty(Stub.ENDPOINT_ADDRESS_PROPERTY, "http://www.whereyoudey.com/WhereyoudeyWebServices/Search.asmx/SearchData");
//		service._setProperty(Stub.SESSION_MAINTAIN_PROPERTY, new Boolean(true));
        String resultAsXml = "";
        Result[] result = null;
        try {
            resultAsXml = getResult(service, name, location, filter);
            System.out.println(resultAsXml);
            result = processResponse(resultAsXml);
        } catch (XmlPullParserException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return result;
    }

    private String getResult(Search_Stub service, String name, String location, ArrayOfString filter) throws RemoteException {
        String result;
        String[] searchResult = service.SearchData(name, location, filter).getString();
        result = searchResult[0];
        return result;
    }

    private Result[] processResponse(String result) throws XmlPullParserException, IOException {
        final byte[] resultBytes = result.getBytes();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(resultBytes);
        InputStreamReader inputStreamReader = new InputStreamReader(byteArrayInputStream);
        KXmlParser parser = new KXmlParser();
        parser.setInput(inputStreamReader);

        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "SearchResults");
        parser.nextTag();

        int eventCode = parser.getEventType();
        String tagName = "";
        Result resultObj = null;
        int resultCount = 0;
        Result[] resultList = new Result[MAX_RESULTS];

        boolean processingResult = false;
        boolean reachedMaxResults = false;

        while (eventCode != XmlPullParser.END_DOCUMENT && !reachedMaxResults) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if ("Result".equals(tagName)) {
                        resultObj = new Result();
                        processingResult = true;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if ("Result".equals(tagName)) {
                        resultList[resultCount] = resultObj;
                        processingResult = false;
                        resultCount++;
                        if (resultCount == MAX_RESULTS) {
                            reachedMaxResults = true;
                        }
                    }
                    break;
                case XmlPullParser.TEXT:
                    String text = parser.getText();
                    if (processingResult) {
                        resultObj.setProperty(tagName, text);
                    }
                    break;
            }
            parser.next();
            eventCode = parser.getEventType();
        }
        return resultList;
    }
}
