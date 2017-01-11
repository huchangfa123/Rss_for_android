package zhp.rssbook;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by huchangfa on 2016/12/20.
 */
public class Signup_activity extends Activity{

    private View rootview;

    private EditText email;
    private EditText password;
    private Button signup;

    SharedPreferences share = null;
    SharedPreferences.Editor editor = null;

    public void onCreate(Bundle savedInstanceState){

        super.onCreate( savedInstanceState );
        rootview = getLayoutInflater().inflate(R.layout.signup,null);
        setContentView(rootview);

        initView();
        initEvent();
    }

    private void initEvent() {
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                http_deal mhttp = new http_deal("https://www.enjoyrss.com/auth/register");
                mhttp.doPost_signup(email.getText().toString(),password.getText().toString());
                if(mhttp.getjudge()){
                    share = getSharedPreferences("login", Context.MODE_PRIVATE);
                    editor = share.edit();
                    editor.putString("user",email.getText().toString());
                    editor.putString("password",password.getText().toString());
                    editor.putBoolean("isfirst", false);
                    editor.commit();
                    Intent intent = new Intent();
                    intent.setClass(Signup_activity.this, Login_Activity.class);
                    Signup_activity.this.startActivity(intent);
                }
                else
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Signup_activity.this);
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

    private void initView() {
        email = (EditText) rootview.findViewById(R.id.user_email);
        password = (EditText) rootview.findViewById(R.id.user_password);
        signup = (Button) rootview.findViewById(R.id.sign);
    }
}
