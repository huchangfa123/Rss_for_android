package zhp.rssbook;

import android.graphics.Bitmap;

/**
 * Created by huchangfa on 2016/11/29.
 */
public class dingyueyuan_specific_content_model {

    private String title;
    private String feednum;
    private String link;
    private String feed_time;
    private Bitmap pic;


    public dingyueyuan_specific_content_model(String title,String feednum,String link,String feed_time,Bitmap pic){
        this.title = title;
        this.feednum = feednum;
        this.link = link;
        this.feed_time = feed_time;
        this.pic = pic;
    }

    public void setTitle(String str) {this.title = str;}
    public void setFeednum(String feednum) {this.feednum = feednum;}
    public void setLink(String link) {this.link = link;}
    public void setFeed_time(String feed_time) {this.feed_time = feed_time;}
    public void setPic(Bitmap pic) {this.pic = pic;}

    public String getTitle() {return  this.title;}
    public String getFeednum() {return  this.feednum;}
    public String getLink() {return this.link;}
    public String getFeed_time() {return this.feed_time;}
    public Bitmap getPic() {return  this.pic;}
}
