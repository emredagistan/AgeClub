package com.example.administrator.age_101;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class DiscountAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private List<Discount> discounts;
    private Context context;

    public DiscountAdapter(Activity activity, Context context) {
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    @Override
    public int getCount() {
        return discounts.size();
    }

    @Override
    public Discount getItem(int position) {
        return discounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View elementView;

        elementView = mInflater.inflate(R.layout.list_element_discounts, null);

        TextView textView = elementView.findViewById(R.id.discountText);
        ImageView logo = elementView.findViewById(R.id.discountLogo);
        ImageView image = elementView.findViewById(R.id.discountImage);

        Discount dc = discounts.get(i);

        textView.setText(dc.getCampaignName());
        Picasso.with(context).load(dc.getLogoUrl()).into(logo);
        Picasso.with(context).load(dc.getImageLink()).into(image);

        return elementView;
    }


}
