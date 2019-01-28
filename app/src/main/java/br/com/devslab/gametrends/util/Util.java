package br.com.devslab.gametrends.util;

import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class Util {

    public static Boolean isEmptyOrNull(Object o){

        Boolean condition = Boolean.FALSE;

        if(o == null){
            condition = Boolean.TRUE;
        }else if(o instanceof List){
            List list = (List) o;
            if(list.isEmpty()){
                condition = Boolean.TRUE;
            }
        }

        return condition;
    }

    public static void showSnack(View view, String msg){

        Snackbar snack = Snackbar.make(view, msg, Snackbar.LENGTH_SHORT);
        View snackView = snack.getView();
        TextView tv = snackView.findViewById(android.support.design.R.id.snackbar_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        } else {
            tv.setGravity(Gravity.CENTER_HORIZONTAL);
        }

        snack.show();
    }

    public static void loadImg(String from, ImageView imageView){
        Glide.with(imageView.getContext()).load(from).into(imageView);
    }
}
