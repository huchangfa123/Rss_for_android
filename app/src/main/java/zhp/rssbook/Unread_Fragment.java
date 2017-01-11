package zhp.rssbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

public class Unread_Fragment extends Fragment {

    private View rootview;

    private ListView mlistview;
    private List<Unread_Model> mlist;
    private MainActivity activity;
    private Unread_Adapt madapter;

    private RelativeLayout loading;
    private LinearLayout tab01_view;

    public static final int REQUSET = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.mtab01, container, false);
        activity = (MainActivity)getActivity();
        initView();
        fillData();
        initData();
        initEvents();
        return rootview;
    }

    private void fillData() {
        new AsyncTask<Void,Void,List<Unread_Model>>(){

            @Override
            protected void onPreExecute() {
                loading.setVisibility(View.VISIBLE);
                tab01_view.setVisibility(View.INVISIBLE);
                super.onPreExecute();
            }

            @Override
            protected List<Unread_Model> doInBackground(Void... params) {
                initData();
                return mlist;
            }

            @Override
            protected void onPostExecute(List<Unread_Model> result){

                loading.setVisibility(View.INVISIBLE);
                tab01_view.setVisibility(View.VISIBLE);
                madapter = new Unread_Adapt(activity,mlist);
                mlistview.setAdapter(madapter);
                super.onPostExecute(result);
            }
        }.execute();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUSET)
            fillData();
    }

    private void initEvents() {
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                Unread_Adapt adapter = (Unread_Adapt) parent.getAdapter();
                Unread_Model content = (Unread_Model) adapter.getItem(position);
                String feedid = content.getFeed_id();
                bundle.putString("title",content.getText());
                bundle.putString("feed_id",feedid);
                bundle.putString("xsrf",activity.getxsrf());
                bundle.putString("cookie",activity.getcookie());
                intent.putExtras(bundle);
                intent.setClass(activity,Reading_Activity.class);
                startActivityForResult(intent,REQUSET);
            }
        });
    }

    private void initData() {
        mlist = new ArrayList<Unread_Model>();
        http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/posts/recent");
        mhttp.get_unread_list(mhttp.getUrl(),activity.getxsrf(),activity.getcookie());
        mlist = mhttp.get_content_list5();
    }

    private void initView() {
        mlistview = (ListView) rootview.findViewById(R.id.unread_list);
        loading = (RelativeLayout) rootview.findViewById(R.id.loading);
        tab01_view = (LinearLayout) rootview.findViewById(R.id.tab1_view);
    }
}
