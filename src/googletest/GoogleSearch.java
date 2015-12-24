package googletest;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class GoogleSearch {
  private int SiteRankNun=30;
  public GoogleSearch() {
    // TODO Auto-generated constructor stub
  }
  
  public List<SearchModel> serch(String word) throws ClientProtocolException, IOException{

    StringBuffer buf =new StringBuffer();
    String url;
    buf.append("http://www.google.co.jp/search?&num=");
    buf.append(SiteRankNun);
    buf.append("&q=");
    buf.append(URLEncoder.encode( word, "utf-8" ));
    url=buf.toString();

    CloseableHttpClient client = HttpClientBuilder.create().build();
    HttpGet httpGet = new HttpGet( url );
    ResponseHandler<List<SearchModel>> handler = new GoogleSearchResponseHandler();
    List<SearchModel> list = client.execute( httpGet, handler );

    return list;
  }
}


