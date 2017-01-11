package zhp.rssbook;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class hot_Fragment extends Fragment {

    private View rootview;
    private ListView mlistview;
    private ImageButton search;
    private EditText mEditText;
    private MainActivity activity;

    private Hot_Adapt madapter;
    private List<Hot_model> mlist;
    private http_deal mhttp;

    private RelativeLayout loading;
    private LinearLayout hot_view;

    public static final int REQUSET = 1;


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        rootview = inflater.inflate(R.layout.mtab04, container, false);
        activity = (MainActivity)getActivity();
        
        initView();
        fillData();
        initEvent();
        return rootview;
    }

    private void fillData() {
        new AsyncTask<Void,Void,List<Hot_model>>(){

            @Override
            protected void onPreExecute() {
                loading.setVisibility(View.VISIBLE);
                hot_view.setVisibility(View.INVISIBLE);
                super.onPreExecute();
            }

            @Override
            protected List<Hot_model> doInBackground(Void... params) {
                initData();
                return mlist;
            }

            @Override
            protected void onPostExecute(List<Hot_model> result){

                loading.setVisibility(View.INVISIBLE);
                hot_view.setVisibility(View.VISIBLE);
                madapter = new Hot_Adapt(activity,result);
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

    private void initEvent() {
        mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("xsrf",activity.getxsrf());
                bundle.putString("cookie",activity.getcookie());
                Hot_Adapt madapter = (Hot_Adapt) parent.getAdapter();
                Hot_model Feed_id = (Hot_model)madapter.getItem(position);
                String str = Feed_id.getText2();
                Bitmap pic = Feed_id.getImageView();
                bundle.putString("title",Feed_id.getText());
                bundle.putString("feed_id",str);
                bundle.putParcelable("pic",pic);
                intent.putExtras(bundle);
                intent.setClass(activity, dingyueyuan_Message_Activity.class);
                startActivity(intent);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = mEditText.getText().toString();
                http_deal mhttp = new http_deal("https://www.enjoyrss.com/api/feed");
                mhttp.search_dingyueyuan(mhttp.getUrl(),activity.getxsrf(),activity.getcookie(),url);

                if(mhttp.getjudge()){
                    String feedid = mhttp.getfeedid();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putString("feed_id",feedid);
                    bundle.putString("xsrf",activity.getxsrf());
                    bundle.putString("cookie",activity.getcookie());

                    String url2 = "https://www.enjoyrss.com/api/feed/" + feedid;
                    mhttp.get_specific_content(url2,activity.getxsrf(),activity.getcookie());
                    dingyueyuan_specific_content_model mmodel = mhttp.get_message();
                    bundle.putString("title",mmodel.getTitle());
                    bundle.putParcelable("pic",mmodel.getPic());

                    intent.putExtras(bundle);
                    intent.setClass(activity,dingyueyuan_Message_Activity.class);
                    startActivityForResult(intent,REQUSET);
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String error = mhttp.getError();
                    builder.setTitle("提示");
                    builder.setMessage(error);
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }

            }
        });
    }

    private void initData() {
        mhttp = new http_deal("https://www.enjoyrss.com/api/feeds");
        String url = "https://www.enjoyrss.com/api/feeds?" + "order=feedNum&per_page=20&page=0";
        Context context = this.getActivity();
        mhttp.get_hot_dingyueyuan(url,activity.getxsrf(),activity.getcookie());
        mlist = new ArrayList<Hot_model>();
        mlist = mhttp.get_content_list4();
    }

    private void initView() {
        mlistview = (ListView) rootview.findViewById(R.id.hot_list);
        mEditText = (EditText) rootview.findViewById(R.id.search_content);
        search = (ImageButton) rootview.findViewById(R.id.search_button);
        loading = (RelativeLayout) rootview.findViewById(R.id.loading);
        hot_view = (LinearLayout) rootview.findViewById(R.id.hot_view);
    }
}
