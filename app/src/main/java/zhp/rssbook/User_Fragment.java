package zhp.rssbook;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class User_Fragment extends Fragment {
    private View rootview;
    private EditText mEditText;
    private ImageView mImageView;
    private Button mbutton;

    private String xsrf;
    private String cookie;
    private MainActivity activity;

    private http_deal http_user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.user_message, container, false);
        activity = (MainActivity)getActivity();
        initView();
        initData();
        initEvents();

        return rootview;
    }

    private void initEvents() {
        mbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.enjoyrss.com/api/user";
                http_user.put_username(url,activity.getxsrf(),activity.getcookie(),mEditText.getText().toString());
                if(http_user.getjudge()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    builder.setTitle("提示");
                    builder.setMessage("修改成功！");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();

                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                    String error = http_user.getError();
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

        http_user = new http_deal("https://www.enjoyrss.com");
        http_user.doPost_get_user("https://www.enjoyrss.com/api/user",activity.getxsrf(),activity.getcookie());
        mImageView.setImageBitmap(http_user.get_Mimg());
        
        mEditText.setText(http_user.getUse_name());
    }

    private void initView() {
        mEditText = (EditText) rootview.findViewById(R.id.user_name);
        mbutton = (Button) rootview.findViewById(R.id.button);
        mImageView = (ImageView) rootview.findViewById(R.id.user_pic) ;
    }
}
