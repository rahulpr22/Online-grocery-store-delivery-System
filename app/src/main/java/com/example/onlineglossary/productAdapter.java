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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
@SuppressWarnings({"unchecked","unsafe"})
public class productAdapter extends ArrayAdapter {
    private Context mcontext;
    public productAdapter(@NonNull Context context, ArrayList<productgrid> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
        this.mcontext=context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.gridviewitem, parent, false);
        }
        productgrid product = (productgrid) getItem(position);
        TextView title = listitemView.findViewById(R.id.prod_title);
        TextView price = listitemView.findViewById(R.id.prod_price);
        //TextView desc = listitemView.findViewById(R.id.prod_description);
        ImageView logo = listitemView.findViewById(R.id.prod_image);
        TextView view =listitemView.findViewById(R.id.viewbtn);
        title.setText(product.getTitle());
        price.setText(product.getPrice());
        //desc.setText(product.getDescription());
        //Log.d("test2","logo "+product.getLogo());
        Picasso.get().load(product.getLogo()).into(logo);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mcontext,productScreen.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                i.putExtra("title",product.getTitle());
                i.putExtra("price",product.getPrice());
                i.putExtra("desc",product.getDescription());
                i.putExtra("logo",product.getLogo());
                i.putExtra("pid",product.getPid());
                i.putExtra("catid",product.getCatid());
                mcontext.startActivity(i);
            }
        });

        return listitemView;
    }

}
