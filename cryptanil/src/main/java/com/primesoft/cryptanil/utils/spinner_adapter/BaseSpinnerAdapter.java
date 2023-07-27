package com.primesoft.cryptanil.utils.spinner_adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;

import androidx.annotation.LayoutRes;

import java.util.List;

public abstract class BaseSpinnerAdapter<D, H> extends BaseAdapter implements SpinnerAdapter {
    private final Context context;
    private List<D> data;

    public BaseSpinnerAdapter(Context context, List<D> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getDropDownView(i, view, viewGroup);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        View row;
        H viewHolder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = layoutInflater.inflate(getLayoutResId(), null);
            viewHolder = onCreateViewHolder(row, parent, position);
            row.setTag(viewHolder);
        } else {
            row = convertView;
            viewHolder = (H) row.getTag();
        }
        onBindView(viewHolder, position);
        return row;
    }

    public List<D> getData() {
        return data;
    }

    public void setData(List<D> data) {
        this.data = data;
    }

    protected Context getContext() {
        return context;
    }

    protected abstract H onCreateViewHolder(View rowView, ViewGroup parent, int position);

    protected abstract void onBindView(H viewHolder, int position);

    @LayoutRes
    protected abstract int getLayoutResId();
}

