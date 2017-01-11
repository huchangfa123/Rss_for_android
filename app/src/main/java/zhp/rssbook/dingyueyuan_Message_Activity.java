package zhp.rssbook;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by huchangfa on 2016/11/23.
 */
public class dingyueyuan_Message_Activity extends ActionBarActivity {
    private View rootview;

    private TextView mtextview;
    private ListView mlistview;
    private ImageView mimageview;
    private TextView mtitle;
    private ListView papertitle;
    private Toolbar mtoolbar;
    private Paper_Adapter madapter;

    private Intent intent;
    private Bundle mbundle;

    private ImageView dingyue_img;
    private TextView dingyue_text;

    private LinearLayout dingyue;
    private boolean judge_dingyue;

    public static final int REQUSET = 1;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        rootview = getLayoutInflater().inflate(R.layout.mtab02_itemclick,null);
        setContentView(rootview);

        initView();
        initData();
        initToolbar();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET)
            initData();
    }

    private void initToolbar() {

        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        mtoolbar.setNavigationIcon(upArrow);
        mtoolbar.setTitle("");
        mtoolbar.setSubtitle("");
        mtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initData() {
        intent = getIntent();
        mbundle = intent.getExtras();

        mimageview.setImageBitmap((Bitmap)mbundle.getParcelable("pic"));
        mtitle.setText(mbundle.getString("title"));

        final http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/feed");
        String url = "https://www.enjoyrss.com/api/feed/" + mbundle.getString("feed_id");
        mhttp.get_specific_content(url,mbundle.getString("xsrf"),mbundle.getString("cookie"));
        String result = "标题                      " + mhttp.get_message().getTitle() + "\n"
                      + "订阅人数              " + mhttp.get_message().getFeednum() + "\n"
                      + "订阅源网站          " + mhttp.get_message().getLink();

        mtextview.setText(result + "\n");

        if(mhttp.get_message().getFeed_time()!= null){
            judge_dingyue = true;
            dingyue_img.setImageResource(R.drawable.eye_close);
            dingyue_text.setText("取消订阅");
        }
        else{
            judge_dingyue = false;
            dingyue_img.setImageResource(R.drawable.eye);
            dingyue_text.setText("订阅");
        }

        dingyue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.enjoyrss.com/api/feed/" + mbundle.getString("feed_id");
                if(judge_dingyue)
                {
                    mhttp.delete_dingyue(url,mbundle.getString("xsrf"),mbundle.getString("cookie"));
                    dingyue_img.setImageResource(R.drawable.eye);
                    dingyue_text.setText("订阅");
                    judge_dingyue = false;
                }
                else{
                    mhttp.put_dingyue(url,mbundle.getString("xsrf"),mbundle.getString("cookie"));
                    dingyue_img.setImageResource(R.drawable.eye_close);
                    dingyue_text.setText("取消订阅");
                    judge_dingyue = true;
                }

            }
        });

        String url2 = "https://www.enjoyrss.com/api/posts?feed_id=" + mbundle.getString("feed_id") +"&page=0&per_page=99999";
        mhttp.get_paper(url2,mbundle.getString("xsrf"),mbundle.getString("cookie"));
        madapter = new Paper_Adapter(this,mhttp.get_content_list2());

        papertitle.setAdapter(madapter);

        papertitle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent2 = new Intent();
                Bundle bundle2 = new Bundle();
                bundle2.putString("xsrf",mbundle.getString("xsrf"));
                bundle2.putString("cookie",mbundle.getString("cookie"));
                Paper_Adapter adapter = (Paper_Adapter) parent.getAdapter();
                paper_ContentModel feed_id = (paper_ContentModel) adapter.getItem(position);
                bundle2.putString("feed_id",feed_id.getFeed_id());
                bundle2.putString("title",feed_id.getTitle());
                bundle2.putBoolean("star",feed_id.getStar());
                bundle2.putBoolean("zan",feed_id.getzan());
                intent2.putExtras(bundle2);
                intent2.setClass(dingyueyuan_Message_Activity.this,Reading_Activity.class);
                startActivityForResult(intent2,REQUSET);
            }
        });


    }

    private void initView() {
        mtitle = (TextView)rootview.findViewById(R.id.dingyueyuan_title);
        mimageview = (ImageView)rootview.findViewById(R.id.pic);
        mtextview = (TextView)rootview.findViewById(R.id.dingyueyuan_message);
        papertitle = (ListView)rootview.findViewById(R.id.paper_title);
        mtoolbar = (Toolbar)rootview.findViewById(R.id.toolbar2);

        dingyue_img = (ImageView)rootview.findViewById(R.id.eye_icon);
        dingyue_text = (TextView)rootview.findViewById(R.id.dingyuema);
        dingyue = (LinearLayout)rootview.findViewById(R.id.dingyue);

        setSupportActionBar(mtoolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }
}
