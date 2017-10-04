package com.tony_justin.mobile_app.assassins;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.tony_justin.mobile_app.assassin.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class ExpendableListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHashMap;
    public ExpendableListAdapter(Context context, List<String> listDataHead, HashMap<String, List<String>> listHashMap) {
        this.context = context;
        this.listDataHeader = listDataHead;
        this.listHashMap = listHashMap;
    }
    @Override
    public int getGroupCount() {
        return listDataHeader.size();
    }
    @Override
    public int getChildrenCount(int groupPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).size();
    }
    @Override
    public Object getGroup(int groupPosition) {
        return listDataHeader.get(groupPosition);
    }
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return listHashMap.get(listDataHeader.get(groupPosition)).get(childPosition);
    }
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View view, ViewGroup parent) {
        String headerTitle = (String)getGroup(groupPosition);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_group, null);
        }
        TextView lblListHeader = (TextView)view.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);
        return view;
    }
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View view, ViewGroup parent) {
        final String childText = (String)getChild(groupPosition, childPosition);
        if (view == null){
            LayoutInflater inflater = (LayoutInflater)this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.list_item_2, null);
        }
        TextView txtListChild = (TextView)view.findViewById(R.id.lblListItem);
        txtListChild.setText(childText);
        return view;
    }
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
