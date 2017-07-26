package com.james.luckwheel;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.james.luckwheel.view.EnvironmentLayout;
import com.james.luckwheel.view.LuckPanLayout;
import com.james.luckwheel.view.RotatePan;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RotatePan.AnimationEndListener {
    private EnvironmentLayout layout;
    private RotatePan rotatePan;
    private LuckPanLayout luckPanLayout;
    private TextView hit_items;
    private ImageView goBtn;
    private ImageView yunIv;
    private String[] item;
    private String[] strs;
    private Button btnDiglog, btnReadNext;
    private EditText editText1, editText2, editText3, editText4, editText5, editText6;
    private String[] customStrArray = new String[6];
    private String TAG = MainActivity.class.getSimpleName();
    private TextView txt_Spotline;
    private TinyDB tinydb;
    private CustomDialogClass cusDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        cusDialog = new CustomDialogClass(MainActivity.this);
        tinydb = new TinyDB(MainActivity.this);
        txt_Spotline = (TextView) findViewById(R.id.hit_user_tv);
        hit_items = (TextView) findViewById(R.id.hit_user_tv);
        strs = getResources().getStringArray(R.array.names);
        luckPanLayout = (LuckPanLayout) findViewById(R.id.luckpan_layout);
        luckPanLayout.startLuckLight();
        rotatePan = (RotatePan) findViewById(R.id.rotatePan);
        rotatePan.setAnimationEndListener(this);
        goBtn = (ImageView) findViewById(R.id.go);
        yunIv = (ImageView) findViewById(R.id.yun);
        hit_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog();
            }
        });
        luckPanLayout.post(new Runnable() {
            @Override
            public void run() {
                int height = getWindow().getDecorView().getHeight();
                int width = getWindow().getDecorView().getWidth();

                int backHeight = 0;

                int MinValue = Math.min(width, height);
                MinValue -= Util.dip2px(MainActivity.this, 10) * 2;
                backHeight = MinValue / 2;

                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) luckPanLayout.getLayoutParams();
                lp.width = MinValue;
                lp.height = MinValue;
                luckPanLayout.setLayoutParams(lp);

                MinValue -= Util.dip2px(MainActivity.this, 28) * 2;
                lp = (RelativeLayout.LayoutParams) rotatePan.getLayoutParams();
                lp.height = MinValue;
                lp.width = MinValue;

                rotatePan.setLayoutParams(lp);


                lp = (RelativeLayout.LayoutParams) goBtn.getLayoutParams();
                lp.topMargin += backHeight;
                lp.topMargin -= (goBtn.getHeight() / 2);
                goBtn.setLayoutParams(lp);
                getWindow().getDecorView().requestLayout();
            }
        });
    }

    public void rotation(View view) {
        rotatePan.startRotate(-1);
        luckPanLayout.setDelayTime(100);
        goBtn.setEnabled(false);
    }

    @Override
    public void endAnimation(int position) {
        goBtn.setEnabled(true);
        luckPanLayout.setDelayTime(500);
        cusDialog.showDialog(MainActivity.this, strs[position]);
        //Toast.makeText(this, "Position = " + position + "," + strs[position], Toast.LENGTH_SHORT).show();
    }
    public int randomSun(){
        int j = (int)(Math.random()* 9 + 1);
        return j;
    }

    public void showDialog() {
        AlertDialog.Builder optionDialog = new AlertDialog.Builder(this);
        FrameLayout frameLayout = new FrameLayout(optionDialog.getContext());
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View myView = inflater.inflate(R.layout.activity_dialog, frameLayout);
        btnDiglog = (Button) myView.findViewById(R.id.dialog_btn);
        btnReadNext = (Button) myView.findViewById(R.id.btn_readData);
        editText1 = (EditText) myView.findViewById(R.id.editText3);
        editText2 = (EditText) myView.findViewById(R.id.editText4);
        editText3 = (EditText) myView.findViewById(R.id.editText5);
        editText4 = (EditText) myView.findViewById(R.id.editText6);
        editText5 = (EditText) myView.findViewById(R.id.editText7);
        editText6 = (EditText) myView.findViewById(R.id.editText8);
        optionDialog.setView(frameLayout);
        final AlertDialog alert = optionDialog.create();
        final ArrayList a = new ArrayList();
        btnReadNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tinydb.getListString("customer").size() != 0) {
                    tinydb.getListString("customer");
                    editText1.setText(tinydb.getListString("customer").get(0));
                    editText2.setText(tinydb.getListString("customer").get(1));
                    editText3.setText(tinydb.getListString("customer").get(2));
                    editText4.setText(tinydb.getListString("customer").get(3));
                    editText5.setText(tinydb.getListString("customer").get(4));
                    editText6.setText(tinydb.getListString("customer").get(5));
                } else {
                    Toast.makeText(MainActivity.this, "沒有上一次的紀錄", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnDiglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customStrArray[0] = editText1.getText().toString();
                customStrArray[1] = editText2.getText().toString();
                customStrArray[2] = editText3.getText().toString();
                customStrArray[3] = editText4.getText().toString();
                customStrArray[4] = editText5.getText().toString();
                customStrArray[5] = editText6.getText().toString();
                Collections.addAll(a, customStrArray);
                rotatePan.setStr(customStrArray);
                txt_Spotline.setText(customStrArray[0] + " 、 " + customStrArray[1] + " 、 " + customStrArray[2] + " 、 " + customStrArray[3] + " 、 " + customStrArray[4] + " 、 " + customStrArray[5]);
                txt_Spotline.setSelected(true);
                strs = customStrArray;
                tinydb.putListString("customer", a);
                alert.dismiss();
            }
        });
        alert.show();

    }
}
