package com.james.luckwheel;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;

public class CustomDialogClass extends Dialog implements android.view.View.OnClickListener {
    public Activity c;
    public Context context;
    public Dialog d;
    public Button yes;
    public TextView txt_String;
    public String result = "";
    KonfettiView viewKonfetti;
    String TAG = CustomDialogClass.class.getSimpleName();

    public CustomDialogClass(Context context) {
        super(context);
        this.context = context;
        viewKonfetti = (KonfettiView)((Activity) context).findViewById(R.id.viewKonfetti);
    }

    public void showDialog(Activity activity, String msg) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.custom_dialog);
        TextView text = (TextView) dialog.findViewById(R.id.txt_dia);
        text.setText(msg + " 中獎");
        Button dialogButton = (Button) dialog.findViewById(R.id.btn_yes);
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void celebrate(){
        viewKonfetti.build()
                .addColors(Color.YELLOW, Color.GREEN, Color.MAGENTA)
                .setDirection(0.0, 359.0)
                .setSpeed(1f, 5f)
                .setFadeOutEnabled(true)
                .setTimeToLive(2000L)
                .addShapes(Shape.RECT, Shape.CIRCLE)
                .setPosition(-50f, viewKonfetti.getWidth() + 50f, -50f, -50f)
                .stream(300, 5000L);
        Log.e(TAG,"viewKonfetti.getWidth() : " + viewKonfetti.getWidth());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    public void setStr(String present) {
        Log.e(TAG, present);
        this.result = present;
    }
}
