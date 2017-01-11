package zhp.rssbook;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private List<ContentModel> list;
    private ListView mDrawerList;
    private drawer_ContentAdapter adapter;

    private LinearLayout mTabunread;
    private LinearLayout mTabdiyueyuan;
    private LinearLayout mTabstarpaper;
    private LinearLayout mTabhot;

    private ImageButton mTabunread_img;
    private ImageButton mTabdiyueyuan_img;
    private ImageButton mTabstarpaper_img;
    private ImageButton mTabhot_img;

    private Fragment mtab01;
    private Fragment mtab02;
    private Fragment mtab03;
    private Fragment mtab04;
    private Fragment use_message;

    private TextView mtest;

    private Intent intent;
    private Bundle mbundle;

    private String xsrf,cookie;

    SharedPreferences share = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        inittoolbar();
        setToolbar();
        init_httpData();
        init_listData();
        adapter = new drawer_ContentAdapter(this, list);
        mDrawerList.setAdapter(adapter);
        initView();
        initEvens();
        resetImgs();
        setSelect(0);
    }


    private void init_httpData() {
        intent = getIntent();
        mbundle = intent.getExtras();
        setxsrf(mbundle.getString("xsrf"));
        setcookie(mbundle.getString("cookie"));
    }

    private void initView() {
        mTabunread = (LinearLayout) findViewById(R.id.tab_unread);
        mTabdiyueyuan = (LinearLayout) findViewById(R.id.tab_diyueyuan);
        mTabstarpaper = (LinearLayout) findViewById(R.id.tab_starpaper);
        mTabhot = (LinearLayout) findViewById(R.id.tab_hot);

        mTabunread_img = (ImageButton) findViewById(R.id.tab_unread_img);
        mTabdiyueyuan_img = (ImageButton) findViewById(R.id.tab_dingyueyuan_img);
        mTabstarpaper_img = (ImageButton) findViewById(R.id.tab_starpaper_img);
        mTabhot_img = (ImageButton) findViewById(R.id.tab_hot_img);
    }

    private void initEvens() {
        mTabunread.setOnClickListener(this);
        mTabdiyueyuan.setOnClickListener(this);
        mTabstarpaper.setOnClickListener(this);
        mTabhot.setOnClickListener(this);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    });
    }


    /*
        给侧边栏初始化列表元素
     */
    private void init_listData() {
        list = new ArrayList<ContentModel>();
        list.add(new ContentModel(R.drawable.users,"个人信息"));
        list.add(new ContentModel(R.drawable.out,"退出登录"));
    }

    private void setToolbar(){
        toolbar.setTitle("Toolbar");//设置Toolbar标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.open, R.string.close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void inittoolbar(){
        toolbar = (Toolbar) findViewById(R.id.tl_custom);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.manager_draw);
        mDrawerList = (ListView)findViewById(R.id.lv_left_menu);
    }

    private void selectItem(int i) {
        Intent intent = new Intent();
        switch (i){
            case 0:
                setSelect(4);
                break;
            case 1:
                share = getSharedPreferences("login", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit();
                editor.putBoolean("isfirst",true);
                editor.commit();
                intent.setClass(MainActivity.this, Login_Activity.class);
                MainActivity.this.startActivity(intent);
                MainActivity.this.finish();
                break;
            default:
                break;
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    private void setSelect(int i){

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        hideFragment(transaction);
        switch (i){
            case 0:
                if(mtab01 == null)
                {
                    mtab01=new Unread_Fragment();
                    transaction.add(R.id.manager_content,mtab01);
                }
                else
                {
                    transaction.show(mtab01);
                }
                mTabunread_img.setImageResource(R.drawable.unread_press);
                toolbar.setTitle("未读");
                break;
            case 1:
                mtab02=new dingyueyuan_Fragment();
                transaction.add(R.id.manager_content,mtab02);
                transaction.show(mtab02);
                mTabdiyueyuan_img.setImageResource(R.drawable.dingyueyuan_press);
                toolbar.setTitle("订阅源");
                break;
            case 2:
                if(mtab03 == null)
                {
                    mtab03=new starpaper_Fragment();
                    transaction.add(R.id.manager_content,mtab03);
                }
                else
                {
                    transaction.show(mtab03);
                }
                mTabstarpaper_img.setImageResource(R.drawable.star_press);
                toolbar.setTitle("收藏");
                break;
            case 3:
                if(mtab04 == null)
                {
                    mtab04=new hot_Fragment();
                    transaction.add(R.id.manager_content,mtab04);
                }
                else
                {
                    transaction.show(mtab04);
                }
                mTabhot_img.setImageResource(R.drawable.square_press);
                toolbar.setTitle("热门");
                break;
            case 4:
                if(use_message == null)
                {
                    use_message=new User_Fragment();
                    transaction.add(R.id.manager_content,use_message);
                }
                else
                {
                    transaction.show(use_message);
                }
                toolbar.setTitle("个人信息");
                break;
            default:
                break;
        }

        transaction.commit();
    }

    private void hideFragment(FragmentTransaction transaction) {
        if(mtab01!=null)
        {
            transaction.hide(mtab01);
        }
        if(mtab02!=null)
        {
            transaction.hide(mtab02);
        }
        if(mtab03!=null)
        {
            transaction.hide(mtab03);
        }
        if(mtab04!=null)
        {
            transaction.hide(mtab04);
        }
        if(use_message!=null)
        {
            transaction.hide(use_message);
        }
    }

    @Override
    public void onClick(View v) {

        resetImgs();
        switch (v.getId())
        {
            case R.id.tab_unread:
                 setSelect(0);
                 break;
            case R.id.tab_diyueyuan:
                 setSelect(1);
                 break;
            case R.id.tab_starpaper:
                 setSelect(2);
                 break;
            case R.id.tab_hot:
                 setSelect(3);
                 break;
            default:
                 break;
        }
    }



    private void resetImgs(){
        mTabunread_img.setImageResource(R.drawable.unread);
        mTabdiyueyuan_img.setImageResource(R.drawable.dingyueyuan);
        mTabstarpaper_img.setImageResource(R.drawable.star);
        mTabhot_img.setImageResource(R.drawable.square);
    }

    public String getxsrf(){
        return this.xsrf;
    }

    public void setxsrf(String str){
        this.xsrf = str;
    }

    public String getcookie(){
        return this.cookie;
    }

    public void setcookie(String str){
        this.cookie = str;
    }
}
