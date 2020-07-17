package com.gochiusa.wanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Tree;

import java.util.List;

public class TreeAdapter extends ListAdapter<Tree, TreeAdapter.TreeViewHolder> {

    private Context mContext;
    private String DIVISION = "  ";

    public TreeAdapter(Context context, List<Tree> list) {
        super(list);
        mContext = context;
    }

    @NonNull
    @Override
    public TreeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_sort_tree_view, parent, false);
        return new TreeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeViewHolder holder, int position) {
        // 获取数据
        Tree tree = getItem(position);
        // 设置第一层的体系名
        holder.superChapterView.setText(tree.getName());
        // 拼接字符串，以显示第二层的所有分体系的名字
        StringBuilder builder = new StringBuilder();
        for (String eachName : tree.getAllChildrenName()) {
            builder.append(eachName);
            builder.append(DIVISION);
        }
        holder.allChapterView.setText(builder.toString());
    }

    /**
     * 体系的ViewHolder
     */
    protected static final class TreeViewHolder extends RecyclerView.ViewHolder {
        TextView superChapterView;
        TextView allChapterView;

        public TreeViewHolder(@NonNull View itemView) {
            super(itemView);
            superChapterView = itemView.findViewById(R.id.tv_sort_super_chapter);
            allChapterView = itemView.findViewById(R.id.tv_sort_all_chapter);
        }
    }
}
