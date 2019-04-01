package com.example.githubportfolio;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.example.githubportfolio.DataActivity.EXTRA_MESSAGE;
import static com.example.githubportfolio.DataActivity.EXTRA_MESSAGE2;
import static com.example.githubportfolio.DataActivity.EXTRA_MESSAGE3;


// Adapter made by my self to add unit to ListView
public class RepoVisualAdapter extends BaseAdapter implements ListAdapter {
    private List<String> list = new ArrayList<String>();
    private List<String> info_list = new ArrayList<String>();
    private List<String> other_list = new ArrayList<String>();
    private Context context;


    public RepoVisualAdapter(List<String> list, List<String> info_list, List<String> other_list, Context context) {
        this.list = list;
        this.info_list = info_list;
        this.other_list = other_list;
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
            view = inflater.inflate(R.layout.arraylist, null);
        }

        // Handle TextView and display string from your list
        TextView listItemText = (TextView)view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        // Handle buttons and add onClickListeners
        Button linkBtn = (Button)view.findViewById(R.id.link_btn);

        linkBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // Open outside web browser when clicked.
                Intent intent = new Intent(context, RepoActivity.class);
                String title_message = list.get(position);
                String info_message = info_list.get(position);
                String other_message = other_list.get(position);
                intent.putExtra(EXTRA_MESSAGE, title_message);
                intent.putExtra(EXTRA_MESSAGE2, info_message);
                intent.putExtra(EXTRA_MESSAGE3, other_message);
                context.startActivity(intent);
            }
        });

        return view;
    }
}
