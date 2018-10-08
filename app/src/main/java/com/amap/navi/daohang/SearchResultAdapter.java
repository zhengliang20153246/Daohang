package com.amap.navi.daohang;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.amap.api.services.help.Tip;

import java.util.List;

/**
 *
 */
public class SearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private List<Tip> mListTips;
    private LayoutInflater layoutInflater;

    public SearchResultAdapter(Context context, List<Tip> tipList) {
        mContext = context;
        mListTips = tipList;
        layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (mListTips != null) {
            return mListTips.size();
        }
        return 0;
    }


    @Override
    public Object getItem(int i) {
        if (mListTips != null) {
            return mListTips.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        try {
            Holder holder;
            if (view == null) {
                holder = new Holder();
                view = layoutInflater.inflate(R.layout.search_result_item, null);
                holder.mName = (TextView) view.findViewById(R.id.name);
                holder.mAddress = (TextView) view.findViewById(R.id.adress);
                view.setTag(holder);
            } else {
                holder = (Holder) view.getTag();
            }
            if (mListTips == null) {
                return view;
            }

            holder.mName.setText(mListTips.get(i).getName());
            String address = mListTips.get(i).getAddress();
            if (TextUtils.isEmpty(address)) {
                holder.mAddress.setVisibility(View.GONE);
            } else {
                holder.mAddress.setVisibility(View.VISIBLE);
                holder.mAddress.setText(address);
            }
        } catch (Throwable e) {
        }
        return view;
    }

    class Holder {
        TextView mName;
        TextView mAddress;
    }
}
