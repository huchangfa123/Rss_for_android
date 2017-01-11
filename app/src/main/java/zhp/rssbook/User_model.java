package zhp.rssbook;

/**
 * Created by huchangfa on 2017/1/1.
 */
public class User_model {
    private int __v;
    private String avatar;
    private String createdtime;
    private String email;
    private String usename;

    public User_model(int i,String s1,String s2,String s3,String s4){
        this.__v = i;
        avatar = s1;
        createdtime = s2;
        email = s3;
        usename = s4;
    }

    public void setUsename(String str) { this.usename = str;}

    public int get__v() {return  this.__v; }
    public String getAvatar() {return  this.avatar;}
    public String getCreatedtime() {return  this.createdtime;}
    public String getEmail() {return  this.email;}
    public String getUsename() {return  this.usename;}
}
