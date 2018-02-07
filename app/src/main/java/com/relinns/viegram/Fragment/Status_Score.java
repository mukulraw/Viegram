package com.relinns.viegram.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.relinns.viegram.R;

/**
 * Created by win 7 on 5/30/2017.
 */
@SuppressWarnings("ALL")
public class Status_Score extends Fragment {
private TextView neophyte;
    private TextView bonafide;
    private TextView socialite;
    private TextView icon;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v= inflater.inflate(R.layout.status_score,container,false);
        neophyte=(TextView)v.findViewById(R.id.neophyte);
        bonafide=(TextView)v.findViewById(R.id.bonafide);
        socialite=(TextView)v.findViewById(R.id.socialite);
        icon=(TextView)v.findViewById(R.id.icon);
        SpannableString ss = new SpannableString("0 to 9,999 points");

// Span to set text color to some RGB value
        final ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.stats_color));

        ss.setSpan(fcs, 0, 2, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
// make them also bold
        ss.setSpan(fcs, 5, 10, Spannable.SPAN_INCLUSIVE_INCLUSIVE);

        String neo = "<font color='#FD2A04'>0</font> "+" to "+"<font color='#FD2A04'>24,999</font>"+" points ";
        neophyte.setText(Html.fromHtml(neo));

        String bofd = "<font color='#4B366C'>25,000</font> "+" to "+"<font color='#4B366C'>99,999</font>"+" points ";
        bonafide.setText(Html.fromHtml(bofd));

        String social = "<font color='#325B61'>100,000</font> "+" to "+"<font color='#325B61'>999,999</font>"+" points ";
        socialite.setText(Html.fromHtml(social));

        String icon_ = "<font color='#40866C'>1 millon</font> "+" points & above";
        icon.setText(Html.fromHtml(icon_));
                return v;
    }
}
