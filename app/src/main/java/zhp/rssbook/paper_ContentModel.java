package zhp.rssbook;

/**
 * Created by huchangfa on 2016/11/29.
 */
public class paper_ContentModel {
    private String title;
    private String data;
    private String feed_id;
    private boolean read;
    private boolean star;
    private boolean zan;

    public paper_ContentModel(String title,String data,boolean read,String feed_id,boolean star,boolean zan){
        super();
        this.title = title;
        this.data = data;
        this.read = read;
        this.feed_id = feed_id;
        this.star = star;
        this.zan = zan;
    }

    public void setTitle(String str) {this.title = str;}
    public void setData(String str) {this.data = str;}
    public void setRead(boolean read) {this.read = read;}
    public void setFeed_id(String Feedid) {this.feed_id = Feedid;}
    public void setStar(boolean star) {this.star = star;}
    public void setZan(boolean zan) {this.zan = zan;}


    public String getTitle() {return  this.title;}
    public String getData() {return this.data;}
    public boolean getread() {return this.read;}
    public String getFeed_id() {return this.feed_id;}
    public boolean getStar() {return this.star;}
    public boolean getzan() {return this.zan;}
}
