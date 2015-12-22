package googletest;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;


public class googletest {

  public static void main( String[] args ){
	  GoogleSearch search=new GoogleSearch();
	  exel ex=new exel();
	   String word="";
	    word=ex.getSEString(1, 7);//エクセルファイルの指定座標ワードで検索
	    System.out.println(word);
	  try {
		List<SearchModel> list;
		list=search.serch(word);
		System.out.println(list.get(0).getTitle());
		ex.makeCell(2, 7, list.get(0).getTitle());
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  
  }
    

}

/**
 * Google検索の中身をHttpCleanerを使用して解析し、
 * 検索結果のリンク先をList<String>で返す
 */
class GoogleSearchResponseHandler
    implements ResponseHandler<List<SearchModel>> {

  public List<SearchModel> handleResponse(final HttpResponse response)
      throws HttpResponseException, IOException {
    StatusLine statusLine = response.getStatusLine();
    
    // ステータスが400以上の場合は、例外をthrow
    if( statusLine.getStatusCode() >= 400 ) {
      throw new HttpResponseException(statusLine.getStatusCode(),
          statusLine.getReasonPhrase());
    }
    
    // contentsの取得
    HttpEntity entity = response.getEntity();
    String contents = EntityUtils.toString(entity);
    
    // HtmlCleaner召還
    HtmlCleaner cleaner = new HtmlCleaner();
    TagNode node = cleaner.clean( contents );

    // 解析結果格納用リスト
    List<SearchModel> list = new ArrayList<SearchModel>();
    
    // まずliを対象にする
    TagNode[] liNodes = node.getElementsByName("li", true);
    for( TagNode liNode : liNodes ) {
      // クラスがgじゃなかったら、多分リンクじゃないので除外
      if( !"g".equals( liNode.getAttributeByName( "class" ) ) )
        continue;

      SearchModel model = new SearchModel();
      
      // タイトルの取得
      TagNode h3Node = liNode.findElementByName("h3", false);
      if( h3Node == null )
        continue;
      model.setTitle( h3Node.getText().toString() );
      
      // URLの取得
      TagNode aNode = h3Node.findElementByName("a", false);
      if( aNode == null )
        continue;
         model.setHref( aNode.getAttributeByName("href") );
         
         // 概要の取得
         TagNode divNode = liNode.findElementByName("div", false);
         if( divNode == null )
           continue;
         model.setDescription( divNode.getText().toString() );
         
         list.add( model );
    }
    
    return list;
  }
}

/**
 * 検索結果格納クラス
 */
class SearchModel {
  String href;
  String title;
  String description;
  public String getHref() {
    return href;
  }
  public void setHref(String href) {
    this.href = href;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
}