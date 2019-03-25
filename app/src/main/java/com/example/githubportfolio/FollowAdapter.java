package com.example.githubportfolio;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.ArrayList;
import java.util.List;


// Adapter made by my self to add unit to ListView
public class FollowAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private List<String> image_list = new ArrayList<String>();
    private Context context;

    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";


    public FollowAdapter(List<String> list, List<String> image_list, Context context) {
        this.list = list;
        this.image_list = image_list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.followlist, null);
        }

        // Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.follow_list_string);
        ImageView listItemImage = (ImageView)view.findViewById(R.id.follow_image);
        listItemText.setText(list.get(position));
        Ion.with(listItemImage).load(image_list.get(position));


        // Handle buttons and add onClickListeners
        Button linkBtn = (Button)view.findViewById(R.id.follow_link_btn);

        linkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,FollowActivity.class);
                intent.putExtra(EXTRA_MESSAGE, list.get(position));
                context.startActivity(intent);
            }
        });

        return view;
    }

}
