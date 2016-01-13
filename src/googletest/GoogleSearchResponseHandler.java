package googletest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

public class GoogleSearchResponseHandler implements ResponseHandler<List<SearchModel>> {

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

    TagNode[] liNodes = node.getElementsByName("ol", true)[0].getElementsByName("div", true);
    //TagNode[] liNodes = node.evaluateXPath("//ol/div");
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
