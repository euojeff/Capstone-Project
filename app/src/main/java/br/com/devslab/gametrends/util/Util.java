package br.com.devslab.gametrends.util;

import android.content.Context;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import br.com.devslab.gametrends.R;
import br.com.devslab.gametrends.database.entity.Game;

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

    public static String formatedReleaseDate(Game game, Context ctx){
        return Util.dateToString(new Date(game.getReleaseDate()), ctx.getResources().getString(R.string.date_format));
    }

    public static String dateToString(Date date, String pattern){
        SimpleDateFormat formater = new SimpleDateFormat(pattern, Locale.US);
        return formater.format(date);
    }

    public static Long timeMillisToUnixTime(Long time){
        return time / 1000L;
    }

    public static Long unixTimeToTimeMilis(Long time){
        return time * 1000L;
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

    public static String formatedRate(Integer rating, Context ctx){

        StringBuilder formatedRate = new StringBuilder();
        String rateLimit = " / " + ctx.getResources().getString(R.string.rating_limit);
        String none = ctx.getResources().getString(R.string.none);

        if(rating == null){
            formatedRate.append(none).append(rateLimit);
        }else{
            formatedRate.append(rating).append(rateLimit);
        }

        return formatedRate.toString();
    }
}
