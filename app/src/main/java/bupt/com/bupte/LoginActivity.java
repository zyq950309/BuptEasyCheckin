package bupt.com.bupte;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EdgeEffect;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.IOException;
import java.lang.reflect.Method;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends Activity implements View.OnClickListener{//登录页面

    private boolean IsLoginOk=false;//学生身份验证是否成功
    private boolean IsStudent=false;//是否学生身份
    //private boolean IsLogin=false;//是否已经登录了（重新打开的时候是直接登录还是手动登录一次）
    private Student stu=null;

    private String name_in;
    private int id_in;
    private boolean Tag=true,Tag1=false;
    private int sid,depmt,prof,building,room;
    private EditText nameInput,idInput;
    private Button nameclear_button,idclear_button,login_button,tour_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        nameInput=(EditText)findViewById(R.id.name_input);
        idInput=(EditText)findViewById(R.id.id_input);
        nameclear_button=(Button)findViewById(R.id.nameclear_button);
        idclear_button=(Button)findViewById(R.id.idclear_button);
        login_button=(Button)findViewById(R.id.login_button);
        tour_button=(Button)findViewById(R.id.tour_button);

        nameclear_button.setOnClickListener(this);
        idclear_button.setOnClickListener(this);
        login_button.setOnClickListener(this);
        tour_button.setOnClickListener(this);

        nameInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(nameInput.getText().toString().isEmpty()){
                    nameclear_button.setVisibility(View.INVISIBLE);
                    if(!idInput.getText().toString().isEmpty()){
                        login_button.setTextColor(Color.parseColor("#999999"));
                    }
                }else {
                    nameclear_button.setVisibility(View.VISIBLE);
                    if(!idInput.getText().toString().isEmpty()){
                        login_button.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//填写姓名显示清除按钮

        idInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(idInput.getText().toString().isEmpty()){
                    idclear_button.setVisibility(View.INVISIBLE);
                    if(!nameInput.getText().toString().isEmpty()){
                        login_button.setTextColor(Color.parseColor("#999999"));
                    }
                }else {
                    idclear_button.setVisibility(View.VISIBLE);
                    if(!nameInput.getText().toString().isEmpty()){
                        login_button.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }

                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 6 && i!= 11 && i != 16  && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 7 || sb.length() == 12|| sb.length() == 17) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }

                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            idInput.setText(sb.subSequence(0, sb.length() - 1));
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    idInput.setText(sb.toString());
                    idInput.setSelection(index);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });//填写id显示清除按钮

        nameclear_button.setVisibility(View.INVISIBLE);
        idclear_button.setVisibility(View.INVISIBLE);

        //(设置)这个页面是一开始进入的页面
        //（函数）两个隐藏按钮（一键清除），IsNameOk或IsIdOk为true且热点在输入框上时显示，点击清除输入内容
        //（函数）键盘输入姓名时，点击键盘上的确定按钮，热点跳到下一行
        //（布局）登录按钮的位置刚好在数字键盘的上面，保证不会遮挡按钮
        //（函数）身份信息错误Toast显示
        //（函数）学生登录跳转至MainpageActivity,游客模式登录跳转至MainpagetourActivity
        //（变量）跳转时传入学生学号（或数据库序号）
    }

    @Override
    public void onClick(View v){//点击事件
        switch (v.getId()){
            case R.id.nameclear_button://清除输入姓名
                nameInput.setText("");
                nameclear_button.setVisibility(View.INVISIBLE);
                break;
            case R.id.idclear_button://清除输入id
                idInput.setText("");
                idclear_button.setVisibility(View.INVISIBLE);
                break;
            case R.id.login_button://点击登录按钮
                if(!nameInput.getText().toString().isEmpty()&&!idInput.getText().toString().isEmpty()) {
                    String name_input = nameInput.getText().toString();
                    int id_input = Integer.parseInt(idInput.getText().toString());
                    if (search(name_input, id_input)) {
                        IsStudent=true;
                        Intent intent = new Intent(LoginActivity.this, MainpageActivity.class);
                        Student student=stu;
                        //游客跳转到主页，传递bool型"IsStudent"是否学生true
                        //传递学生类student
                        intent.putExtra("IsStudent",IsStudent);
                        intent.putExtra("student",student);
                        startActivity(intent);
                        this.finish();
                    } else {
                        if(Tag) {
                            Toast.makeText(LoginActivity.this, "姓名与身份证不一致", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LoginActivity.this, "数据库连接失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "输入不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tour_button://点击游客登录按钮
                IsStudent=false;
                Intent intent = new Intent(LoginActivity.this, MainpageActivity.class);
                Student student=new Student();
                //游客跳转到主页，传递bool型"IsStudent"是否学生false
                //传递默认的空学生信息
                intent.putExtra("IsStudent",IsStudent);
                intent.putExtra("student",student);
                startActivity(intent);
                this.finish();
                Toast.makeText(LoginActivity.this, "游客模式", Toast.LENGTH_SHORT).show();
        }
    }

//    private void setVisible(Button button){//设置清除按钮可见
//        button.setVisibility(View.VISIBLE);
//    }

    private boolean search(String name, int id){//查询数据库，验证登录成功,返回学生信息
        name_in=name;
        id_in=id;
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = new OkHttpClient();
                FormBody body = new FormBody.Builder()
                        .add("name", name_in)
                        .add("id", "" + id_in)
                        .build();
                Request request = new Request.Builder()
                        .url("http://123.206.90.229/v1/login.php")
                        .post(body)
                        .build();
                try {
                    Response response = okHttpClient.newCall(request).execute();
//                    Log.d("wenti",""+response.code());
                    if (response.isSuccessful()) {
                        String responsedata=response.body().string();
//                        Log.d("wenti",responsedata);
                        Student_info student_info=GsonTools.getPerson(responsedata,Student_info.class);
                        if(student_info.getCode()==0) {
                            if (student_info.getIsLoginOk() == 1) {
                                IsLoginOk = true;
                                sid = Integer.valueOf(student_info.getInfo().getSid());
                                depmt = Integer.valueOf(student_info.getInfo().getDepmt());
                                prof = Integer.valueOf(student_info.getInfo().getProf());
                                building = Integer.valueOf(student_info.getInfo().getBuilding());
                                room = Integer.valueOf(student_info.getInfo().getRoom());
                                stu = new Student(name_in, id_in, sid, depmt, prof, building, room);
                            } else {
                                IsLoginOk = false;
                            }
                            Tag=true;
                        }else {
                            IsLoginOk = false;
                            Tag=false;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Tag1=true;
            }
        }).start();

        try {
            Thread.sleep(800);
        }catch (InterruptedException e){
            e.printStackTrace();
        }

        return IsLoginOk;
    }
}
