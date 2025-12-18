package com.testing.containers;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TestHttpClient {
  public int getStatus(String urlStr) {
    try {
      URL url = URI.create(urlStr).toURL();
      HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
      httpURLConnection.setRequestMethod("GET");

      return httpURLConnection.getResponseCode();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
