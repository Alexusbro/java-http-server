package ru.otus.java.basic.httpserver;

import ru.otus.java.basic.httpserver.app.ItemsService;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
    private String rawRequest;
    private HttpMethod method;
    private String uri;
    private Map<String, String> params;
    private String body;

    public String getUri() {
        return uri;
    }

    public String getBody() {
        return body;
    }

    public String getRoutingUri() {
        return method + " " + uri;
    }

    public String getParameters(String key) {
        return params.get(key);
    }

    public HttpRequest(String rawRequest) {
        this.rawRequest = rawRequest;
        this.parse();
    }

    public void info(boolean showRawRequest) {
        if (showRawRequest) {
            System.out.println(rawRequest);
        }
        System.out.println("Method: " + method);
        System.out.println("URI: " + uri);
        System.out.println("Parameters: " + params);
        System.out.println("Body: " + body);
    }

    private void parse() {
        params = new HashMap<>();
        int startIndex = rawRequest.indexOf(' ');
        int endIndex = rawRequest.indexOf(' ', startIndex + 1);
        method = HttpMethod.valueOf(rawRequest.substring(0, startIndex));
        uri = rawRequest.substring(startIndex + 1, endIndex);
        if (uri.contains("?")) {
            String[] elements = uri.split("[?]");
            uri = elements[0];
            String[] keyValues = elements[1].split("[&]");
            for (String o: keyValues) {
                String[] keyValue = o.split("=");
                params.put(keyValue[0], keyValue[1]);
            }
        }
        if (method == HttpMethod.POST || method == HttpMethod.PUT) {
            body = rawRequest.substring(rawRequest.indexOf("\r\n\r\n") + 4);
        }
    }
}
