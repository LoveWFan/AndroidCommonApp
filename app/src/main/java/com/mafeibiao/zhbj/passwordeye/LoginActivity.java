package com.mafeibiao.zhbj.passwordeye;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.mafeibiao.zhbj.R;
import com.mafeibiao.zhbj.drawerlayoutqq.MainActivity2;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private boolean isHidePwd = true;// 输入框密码是否是隐藏的，默认为true
    private EditText et_pwd;
    private Button button_longin;
    private void initView(){
        et_pwd = (EditText) findViewById(R.id.editText_password);
        button_longin = (Button)findViewById(R.id.button_login);
        final Drawable[] drawables = et_pwd.getCompoundDrawables() ;
        final int eyeWidth = drawables[2].getBounds().width() ;// 眼睛图标的宽度
        final Drawable drawableEyeOpen = getResources().getDrawable(R.drawable.pass) ;
        drawableEyeOpen.setBounds(drawables[2].getBounds());//这一步不能省略
        button_longin.setOnClickListener(this);
        et_pwd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_UP){
                    // getWidth,getHeight必须在这里处理
                    float et_pwdMinX = v.getWidth() - eyeWidth - et_pwd.getPaddingRight() ;
                    float et_pwdMaxX = v.getWidth() ;
                    float et_pwdMinY = 0 ;
                    float et_pwdMaxY = v.getHeight();
                    float x = event.getX() ;
                    float y = event.getY() ;
                    if(x < et_pwdMaxX && x > et_pwdMinX && y > et_pwdMinY && y < et_pwdMaxY){
                        // 点击了眼睛图标的位置
                        isHidePwd = !isHidePwd ;
                        if(isHidePwd){
                            et_pwd.setCompoundDrawables(drawables[0],drawables[1],
                                    drawables[2] ,
                                    drawables[3]);

                            et_pwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        }
                        else {
                            et_pwd.setCompoundDrawables(drawables[0] ,drawables[1],
                                    drawableEyeOpen ,
                                    drawables[3]);
                            et_pwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        }
                    }
                }
                return false;
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_login:
                gotoMainActivity();
                break;
        }
    }

    private void gotoMainActivity() {
        startActivity(new Intent(this, MainActivity2.class));
        finish();
    }
}
