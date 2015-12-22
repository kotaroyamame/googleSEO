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
    int y=ex.getStartGyo();
    int x=ex.getStartRetsu();
    String url=ex.getSEString(1, 3);

    try {
      do{
        word=ex.getSEString(x, y);//エクセルファイルの指定座標ワードで検索
        if(word==null){
          break;		
        }
        List<SearchModel> list;
        list=search.serch(word);
        System.out.println(list.get(0).getTitle());
        String msg="圏外";
        for(SearchModel model:list){
          int t=model.getHref().indexOf(url);
          if(t!=-1){
            msg=String.valueOf(list.indexOf(model)+1);
            break;
          }
        }
        ex.makeCell(x+1, y, msg);
        ex.makeCell(x+2, y, list.get(0).getTitle());
        ex.makeCell(x+3, y, list.get(1).getTitle());
        ex.makeCell(x+4, y, list.get(2).getTitle());
        y++;
      }while(!(word.equals(""))||(word!=null));
    } catch (ClientProtocolException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println("end");

  }


}

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

    HttpEntity entity = response.getEntity();
    String contents = EntityUtils.toString(entity);

    HtmlCleaner cleaner = new HtmlCleaner();
    TagNode node = cleaner.clean( contents );

    List<SearchModel> list = new ArrayList<SearchModel>();

    TagNode[] liNodes = node.getElementsByName("li", true);
    for( TagNode liNode : liNodes ) {
      if( !"g".equals( liNode.getAttributeByName( "class" ) ) ){
        continue;
      }
      SearchModel model = new SearchModel();

      TagNode h3Node = liNode.findElementByName("h3", false);
      if( h3Node == null ){
        continue;
      }
      model.setTitle( h3Node.getText().toString() );

      TagNode aNode = h3Node.findElementByName("a", false);
      if( aNode == null ){
        continue;
      }
      model.setHref( aNode.getAttributeByName("href") );

      TagNode divNode = liNode.findElementByName("div", false);
      if( divNode == null ){
        continue;
      }
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