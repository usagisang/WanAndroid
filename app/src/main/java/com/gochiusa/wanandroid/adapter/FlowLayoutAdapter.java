package com.gochiusa.wanandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.widget.FlowLayout;

import java.util.List;

public class FlowLayoutAdapter extends FlowLayout.Adapter<FlowLayoutAdapter.FlowViewHolder> {

    private List<String> mTextList;

    private ItemClickListener mClickListener;
    public FlowLayoutAdapter(List<String> textList) {
        mTextList = textList;
    }

    @Override
    public FlowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView =  LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_search_flow_layout, parent, false);
        FlowViewHolder viewHolder = new FlowViewHolder(itemView);
        // 设置子项的点击事件
        viewHolder.textView.setOnClickListener((view) -> {
            if (mClickListener != null) {
                mClickListener.onClick(viewHolder.textView.getText());
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FlowViewHolder holder, int position) {
        // 将数据填充在控件上
        holder.textView.setText(mTextList.get(position));
    }

    @Override
    public int getItemCount() {
        return mTextList.size();
    }

    public static class FlowViewHolder extends FlowLayout.ViewHolder {
        TextView textView;
        public FlowViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_search_history_tab);
        }
    }

    /**
     *  尝试将传入的值添加到列表的首位，如果列表中已经存在这个值，那么将值移动到首位
     * @return 数据是否被移动，被移动则为{@code true}
     */
    public boolean addOrMoveToTop(String value) {
        boolean move = mTextList.remove(value);
        add(0, value);
        return move;
    }

    public void setItemClickListener(ItemClickListener listener) {
        mClickListener = listener;
    }

    public void removeAll() {
        mTextList.clear();
    }

    public void addAll(List<String> stringList) {
        mTextList.addAll(stringList);
    }

    public void add(int index, String value) {
        mTextList.add(index, value);
    }

    /**
     *  子项TextView的点击事件，设置为String，方便获取其中的文本
     */
    public interface ItemClickListener {
        void onClick(CharSequence text);
    }
}
