package org.povworld.mq;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.CheckForNull;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


import org.json.JSONObject;

import com.joejernst.http.Request;
import com.joejernst.http.Response;

public class HttpUtil {
  
  private static final Logger logger = Logger.getLogger(HttpUtil.class.getName());
  
  private HttpUtil() {}
  
  public static String get(String url) throws IOException, URISyntaxException {
    
//    CloseableHttpClient bla = HttpClientBuilder.create().setUserAgent("Wget/1.16.3 (linux-gnu)").build();
//    HttpGet request = new HttpGet(url);
//    CloseableHttpResponse response = bla.execute(request);
//    
//    return IOUtil.readToString(response.getEntity().getContent(), Charset.defaultCharset());
//    
    Map<String, List<String>> headers = new HashMap<>();
//    headers.put("User-Agent",
//        Arrays.asList("Mozilla/5.0 (Windows NT 6.2; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/32.0.1667.0 Safari/537.36"));
    headers.put("User-Agent", Arrays.asList("Wget/1.16.3 (linux-gnu)"));
    headers.put("Accept", Arrays.asList("*/*"));     
    headers.put("Accept-Encoding", Arrays.asList("identity"));
    
    
    Request request = new Request(url);
    request.setHeaders(headers);
    Response httpResponse = request.getResource();
    
    logger.log(Level.FINER, "Http GET " + url + ": " + httpResponse);
    
    return httpResponse.getBody();
  }
  
  public static String post(String url, String post, @CheckForNull String authorization) throws IOException {
    Map<String, List<String>> headers = new HashMap<>();
    if (authorization != null) {
      headers.put("Authorization", Arrays.asList("Basic " + new String(Base64.getEncoder().encode(authorization.getBytes("UTF-8")), "US-ASCII")));
    }
    
    Request request = new Request(url);
    request.setBody(post);
    request.setHeaders(headers);
    
    Response response = request.postResource();
    return response.getBody();
  }
  
}
