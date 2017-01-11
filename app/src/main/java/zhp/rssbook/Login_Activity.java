package zhp.rssbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by huchangfa on 2016/10/27.
 */
public class Login_Activity extends Activity{

    private View rootview;

    private EditText user_email;
    private EditText user_password;

    private Button sign;
    private Button login;

    private String url_post;

    private Boolean first = null;
    SharedPreferences share = null;
    SharedPreferences.Editor editor = null;

    private String xsrf,cookie;

    public void onCreate(Bundle savedInstanceState){
        super.onCreate( savedInstanceState );
        rootview = getLayoutInflater().inflate(R.layout.login,null);
        setContentView(rootview);

        initView();
        initData();
    }

    private void initEvents() {

        login.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user_email.getText().toString();
                String password = user_password.getText().toString();
                login(username,password);
            }});

        sign.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Login_Activity.this,Signup_activity.class);
                Login_Activity.this.startActivity(intent);
            }
        });
    }

    private void login(String usernames,String passwords){
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        intent.setClass(Login_Activity.this, MainActivity.class);
        http_deal mhttp_deal = new http_deal(url_post);
        mhttp_deal.doPost_login(usernames,passwords);

        if(mhttp_deal.getjudge()){
            editor.putString("user",usernames);
            editor.putString("password",passwords);
            //判断是不是第一次登陆的变量
            editor.putBoolean("isfirst", false);
            editor.commit();
            xsrf = mhttp_deal.get_xsrf();
            cookie = mhttp_deal.get_Cookie();
            bundle.putString("xsrf",xsrf);
            bundle.putString("cookie",cookie);
            intent.putExtras(bundle);
            Login_Activity.this.startActivity(intent);
            Login_Activity.this.finish();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(Login_Activity.this);
            String error = mhttp_deal.getError();
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

    private void initData() {
        share = getSharedPreferences("login", Context.MODE_PRIVATE);
        editor = share.edit();
        first = share.getBoolean("isfirst", true);
        if(first){
            initEvents();
        }
        else{
            login(share.getString("user",""),share.getString("password",""));
        }
    }

    private void initView() {
        user_email = (EditText)rootview.findViewById(R.id.myemail);
        user_password = (EditText)rootview.findViewById(R.id.mypassword);
        login = (Button)rootview.findViewById(R.id.login_in);
        sign = (Button) rootview.findViewById(R.id.signup);
        url_post = "https://www.enjoyrss.com/auth/login";
    }




}
