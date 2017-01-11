package zhp.rssbook;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;



/**
 * Created by huchangfa on 2016/11/23.
 */
public class Reading_Activity extends ActionBarActivity {

    private View rootview;

    private Toolbar toolbar;
    private TextView message;
    private TextView titles;

    private WebView mwebview;

    private String filePath;

    private Intent intent;
    private Bundle mbundle;

    private String pre_paper;
    private String next_paper;
    private String cookie;
    private String xsrf;
    private String feedid;
    private Boolean if_star;
    private Boolean if_zan;

    private String author;

    private LinearLayout front;
    private LinearLayout next;
    private LinearLayout zan;
    private LinearLayout star;

    private ImageView zan_img;
    private ImageView star_img;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        rootview = getLayoutInflater().inflate(R.layout.reading, null);
        setContentView(rootview);

        initView();
        initData();
        initToolBar();
        initEvents();

    }

    private void initEvents() {

        front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pre_paper!=null){
                    feedid = pre_paper;
                    String S = get_webdata(feedid);
                    mwebview.loadData(S, "text/html; charset=UTF-8", null);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(next_paper!=null){
                    feedid = next_paper;
                    String S = get_webdata(feedid);
                    mwebview.loadData(S, "text/html; charset=UTF-8", null);

                }
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/post/");
                String url = "https://www.enjoyrss.com/api/post/" + feedid;
                mhttp.put_star(url,xsrf,cookie);
                if (if_star){
                    star_img.setImageResource(R.drawable.star);
                    if_star = false;
                }
                else {
                    star_img.setImageResource(R.drawable.star_press);
                    if_star = true;
                }

            }
        });

        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/post/");
                String url = "https://www.enjoyrss.com/api/post/" + feedid;
                mhttp.put_love(url,xsrf,cookie);

                System.out.println("我要看的数据时!!!!!!!!!!!!!!!!!!!!!!!!!!!:" + feedid);
                if (if_zan){
                    zan_img.setImageResource(R.drawable.finger);
                    if_zan = false;
                }
                else{
                    zan_img.setImageResource(R.drawable.finger_press);
                    if_zan = true;
                }
            }
        });
    }

    private void initToolBar() {

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setNavigationIcon(upArrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
//                Intent intent = new Intent(Reading_Activity.this, dingyueyuan_Message_Activity.class);
//                startActivity(intent);
            }
        });

    }

    private void initData() {

        intent = getIntent();
        mbundle = intent.getExtras();

        cookie = mbundle.getString("cookie");
        xsrf = mbundle.getString("xsrf");

        String S = get_webdata(mbundle.getString("feed_id"));
        mwebview.loadData(S, "text/html; charset=UTF-8", null);
    }

    private String get_webdata(String feed_id){

        feedid = feed_id;
        http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/post/");
        String url = "https://www.enjoyrss.com/api/post/" + feed_id;
        mhttp.get_paper_content(url,xsrf,cookie);
        String url2 = "https://www.enjoyrss.com/api/post/" + feed_id;
        mhttp.put_data(url2,xsrf,cookie);
        //System.out.println(mhttp.getContent());
        String content = "";
        content = mhttp.getContent();

        pre_paper = mhttp.getpre();
        next_paper = mhttp.getnext();

        if_star = mhttp.getMark();
        if_zan = mhttp.getLove();

        System.out.println("数据:" + if_zan);


        if(if_star)
            star_img.setImageResource(R.drawable.star_press);
        else
            star_img.setImageResource(R.drawable.star);

        if(if_zan){
            zan_img.setImageResource(R.drawable.finger_press);
        }
        else
            zan_img.setImageResource(R.drawable.finger);

        String css = "<link href=\"//cdn.bootcss.com/github-markdown-css/2.4.1/github-markdown.css\" rel=\"stylesheet\">";
        String S = "<!DOCTYPE html>" + "<html>" + "<head>" + css
                + "<style>img{max-width: 100%; width:auto; height: auto;}body{overflow:hidden}a:visited{color:black}</style>"
                +"</head> <body>" + content + "</body> </html>";
        //System.out.println("HTML：" + S);

        String str;
        str = mbundle.getString("title");
        titles.setText(str);
        TextPaint tp = titles.getPaint();
        tp.setFakeBoldText(true);
        author = mhttp.getauthor();
        if(author!="null")
            message.setText("作者:" + author);
        return S;
    }

    private void initView() {
        //goback = (ImageView) rootview.findViewById(R.id.go_back);
        //title = (TextView) rootview.findViewById(R.id.papertitle);
        titles =(TextView) rootview.findViewById(R.id.titles);
        message = (TextView) rootview.findViewById(R.id.paper_message);
        mwebview = (WebView) rootview.findViewById(R.id.webview);
        toolbar = (Toolbar) rootview.findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        front = (LinearLayout) rootview.findViewById(R.id.front);
        star = (LinearLayout) rootview.findViewById(R.id.star);
        zan = (LinearLayout) rootview.findViewById(R.id.zan);
        next = (LinearLayout) rootview.findViewById(R.id.next);

        zan_img = (ImageView) rootview.findViewById(R.id.zan_img);
        star_img = (ImageView) rootview.findViewById(R.id.star_img);
    }


}
