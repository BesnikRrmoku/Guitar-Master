package com.example.android.guitar.web;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.android.guitar.R;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class WebAdapter extends ArrayAdapter<web> {

    private static final String LOCATION_SEPERATOR = " of ";

    public WebAdapter(Context context, ArrayList<web> webs) {
        super(context,0, webs);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item_web, parent, false);
        }
        web currentWeb = getItem(position);
        String formattedRate = formatRate(currentWeb.getRate());
        TextView rateTextView = (TextView) listItemView.findViewById(R.id.rate);
        rateTextView.setText(formattedRate);

        GradientDrawable rateCircle = (GradientDrawable) rateTextView.getBackground();
        int rateColor = getRateColor(currentWeb.getRate());
        rateCircle.setColor(rateColor);

        String originalLocation = currentWeb.getDescription();
        String nameOffset;
        String primaryLocation;

        if(originalLocation.contains(LOCATION_SEPERATOR)) {
            String [] parts = originalLocation.split(LOCATION_SEPERATOR);
            nameOffset = parts[0] + LOCATION_SEPERATOR;
            primaryLocation = parts[1];
        } else {
            nameOffset = getContext().getString(R.string.name_web);
            primaryLocation = originalLocation;
        }

        TextView locationOffsetView = (TextView) listItemView.findViewById(R.id.name_offset);
        locationOffsetView.setText(nameOffset);
        TextView primaryLocationView = (TextView) listItemView.findViewById(R.id.primary_location);
        primaryLocationView.setText(primaryLocation);

        Date dateObject = new Date(currentWeb.getTimeInMilliseconds());

        final TextView timeTextView = (TextView) listItemView.findViewById(R.id.date);
        String formattedDate = formatDate(dateObject);
        timeTextView.setText(formattedDate);

        TextView dateTextView = (TextView) listItemView.findViewById(R.id.time);
        String formattedTime = formatTime(dateObject);
        dateTextView.setText(formattedTime);

        return listItemView;
    }

    private int getRateColor(double rate) {
        int rateColorResourceId;
        int rateFloor = (int) Math.floor(rate);
        switch (rateFloor) {
            case 0:
            case 1:
                rateColorResourceId = R.color.rate1;
                break;
            case 2:
                rateColorResourceId = R.color.rate2;
                break;
            case 3:
                rateColorResourceId = R.color.rate3;
                break;
            case 4:
                rateColorResourceId = R.color.rate4;
                break;
            case 5:
                rateColorResourceId = R.color.rate5;
                break;
            case 6:
                rateColorResourceId = R.color.rate6;
                break;
            case 7:
                rateColorResourceId = R.color.rate7;
                break;
            case 8:
                rateColorResourceId = R.color.rate8;
                break;
            case 9:
                rateColorResourceId = R.color.rate9;
                break;
            default:
                rateColorResourceId = R.color.rate10plus;
                break;
        }
        return ContextCompat.getColor(getContext(), rateColorResourceId);
    }

    private String formatTime(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL ,yyyy");
        return dateFormat.format(dateObject);
    }

    private String formatDate(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
    private String formatRate(double rate) {
        DecimalFormat rateFormat = new DecimalFormat("0.0");
        return rateFormat.format(rate);
    }
}
