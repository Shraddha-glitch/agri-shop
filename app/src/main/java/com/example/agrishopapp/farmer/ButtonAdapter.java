package com.example.agrishopapp.farmer;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;

public class ButtonAdapter extends BaseAdapter {
    private Context context;
    private List<String> buttonNames;

    public ButtonAdapter(Context context, List<String> buttonNames) {
        this.context = context;
        this.buttonNames = buttonNames;
    }

    @Override
    public int getCount() {
        return buttonNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Button button;

        if (convertView == null) {
            button = new Button(context);
            button.setLayoutParams(new GridView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setPadding(8, 8, 8, 8);
        } else {
            button = (Button) convertView;
        }

        button.setText(buttonNames.get(position));
        button.setBackgroundColor(Color.GREEN);

        return button;
    }
}
