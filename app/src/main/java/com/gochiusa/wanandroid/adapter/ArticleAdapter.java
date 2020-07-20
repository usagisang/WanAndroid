package com.gochiusa.wanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.web.WebViewActivity;

import java.util.List;


public class ArticleAdapter extends FootViewAdapter<Article, FootViewAdapter.NormalViewHolder> {

    private static final String DIVISION = "/";

    /**
     * 上下文
     */
    private Context mContext;


    public ArticleAdapter(@Nullable Context context, List<Article> articleList) {
        super(articleList);
        mContext = context;
    }


    @Override
    public void onBindContentViewHolder(@NonNull NormalViewHolder holder, int position) {
        // 强制类型转换
        ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
        // 获取数据
        Article article = getItem(position);
        // 将数据展示在每个子项的控件上
        contentViewHolder.authorView.setText(article.getAuthor());
        contentViewHolder.timeView.setText(article.getNiceDate());
        contentViewHolder.titleView.setText(article.getTitle());
        // 拼接类别的字符串
        contentViewHolder.typeView.setText(String.format("%s%s%s",
                article.getSuperChapterName(), DIVISION, article.getChapterName()));

    }


    public void hideFootView() {
        mFootView.setVisibility(View.GONE);
    }

    public void showFootView() {
        mFootView.setVisibility(View.VISIBLE);
    }

    /**
     * 辅助方法，生成普通的内容子项
     */
    ContentViewHolder createContentView(ViewGroup parent) {
        // 创建普通的子项
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_main_home_page, parent, false);
        // 创建ViewHolder
        ContentViewHolder viewHolder =  new ContentViewHolder(itemView);
        // 设置子项点击事件
        itemView.setOnClickListener((view) -> {
            // 仅当context不为null时进行跳转
            if (mContext != null) {
                Article article = getItem(viewHolder.getAdapterPosition());
                WebViewActivity.startThisActivity(mContext, article.getLink());
            }
        });
        // 使用子项创建ViewHolder，子项已经封装在其中
        return viewHolder;
    }


    /**
     *  普通列表子项的ViewHolder
     */
    protected static final class ContentViewHolder extends NormalViewHolder {
        TextView titleView;
        TextView authorView;
        TextView timeView;
        TextView typeView;

        ContentViewHolder(@NonNull View itemView) {
            super(itemView, CONTENT_TYPE);
            timeView = itemView.findViewById(R.id.tv_main_send_time);
            typeView = itemView.findViewById(R.id.tv_main_article_type);
            authorView = itemView.findViewById(R.id.tv_main_author);
            titleView = itemView.findViewById(R.id.tv_main_article_title);
        }
    }
}
