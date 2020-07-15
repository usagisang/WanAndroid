package com.gochiusa.wanandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Article;

import java.util.List;


public class HomeArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Article> mArticleList;
    private String mDivision = "/";

    /**
     *  子View的类型为内容类型
     */
    private static int CONTENT_TYPE = 0;

    /**
     * 子View的类型为尾布局类型
     */
    private static int FOOT_TYPE = 1;

    public HomeArticleAdapter(List<Article> articleList) {
        mArticleList = articleList;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT_TYPE) {
            // 创建普通的子项
            View itemView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_main_home_page, parent, false);
            // 使用子项创建ViewHolder，子项已经封装在其中
            return new ContentViewHolder(itemView);
        } else {
            View footView = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.item_list_footer_view, parent, false);
            return new FootViewHolder(footView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
            // 强制类型转换
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            // 获取数据
            Article article = mArticleList.get(position);
            // 将数据展示在每个子项的控件上
            contentViewHolder.authorView.setText(article.getAuthor());
            contentViewHolder.timeView.setText(article.getNiceDate());
            contentViewHolder.titleView.setText(article.getTitle());
            // 拼接类别的字符串
            contentViewHolder.typeView.setText(String.format("%s%s%s",
                    article.getSuperChapterName(), mDivision, article.getChapterName()));
        }
    }

    @Override
    public int getItemCount() {
        return mArticleList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mArticleList.size()) {
            return FOOT_TYPE;
        } else {
            return CONTENT_TYPE;
        }
    }

    /**
     *  普通列表子项的ViewHolder
     */
    protected static final class ContentViewHolder extends RecyclerView.ViewHolder {
        TextView titleView;
        TextView authorView;
        TextView timeView;
        TextView typeView;

        public ContentViewHolder(@NonNull View itemView) {
            super(itemView);
            timeView = itemView.findViewById(R.id.tv_main_send_time);
            typeView = itemView.findViewById(R.id.tv_main_article_type);
            authorView = itemView.findViewById(R.id.tv_main_author);
            titleView = itemView.findViewById(R.id.tv_main_article_title);
        }
    }

    /**
     * 尾布局的ViewHolder
     */
    protected static final class FootViewHolder extends RecyclerView.ViewHolder {
        View footView;
        public FootViewHolder(@NonNull View itemView) {
            super(itemView);
            footView = itemView;
        }
    }
}