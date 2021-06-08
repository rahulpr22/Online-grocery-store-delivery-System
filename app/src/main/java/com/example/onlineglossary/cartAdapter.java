package com.example.onlineglossary;



import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class cartAdapter extends ArrayAdapter<cartHelper> {
   private Context mcontext;


    public cartAdapter(@NonNull Context context, ArrayList<cartHelper> dataModalArrayList) {
        super(context,0,dataModalArrayList);
        this.mcontext=context;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.cartitem, parent, false);
        }
        cartHelper c= (cartHelper) getItem(position);
        TextView title = listitemView.findViewById(R.id.cart_prtitle);
        TextView price = listitemView.findViewById(R.id.cart_prprice);
        TextView qty = listitemView.findViewById(R.id.cart_prcount);
        ImageView logo = listitemView.findViewById(R.id.image_cartlist);
        title.setText(c.getItem());
        price.setText(c.getPrice());
        qty.setText("Qty : "+c.getQuantity());
        Picasso.get().load(c.getLogo()).into(logo);


        return listitemView;
    }


}
