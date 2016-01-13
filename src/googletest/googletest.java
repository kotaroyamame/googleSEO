package googletest;
import java.io.IOException;
import java.util.List;
import org.apache.http.client.ClientProtocolException;


public class googletest {

  public static void main( String[] args ){
    System.setProperty("proxySet", "false");
    GoogleSearch search=new GoogleSearch();
    exel ex=new exel("testExample.xls",1,7);//エクセルファイル名,先頭検索ワードの座標x,先頭検索ワードの座標y
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
        for(int i=0;i<10;i++){
          ex.makeCell(x+i+2, y, list.get(i).getTitle());
        }
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