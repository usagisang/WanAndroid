package com.gochiusa.wanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Article;
import com.gochiusa.wanandroid.tasks.web.WebViewActivity;

import java.util.List;


public class ArticleAdapter extends ListAdapter<Article, RecyclerView.ViewHolder> {

    private static final String DIVISION = "/";

    /**
     *  子View的类型为内容类型
     */
    private static int CONTENT_TYPE = 0;

    /**
     * 子View的类型为尾布局类型
     */
    private static int FOOT_TYPE = 1;

    /**
     * 尾布局
     */
    private View mFootView;
    /**
     * 上下文
     */
    private Context mContext;


    public ArticleAdapter(@Nullable Context context, List<Article> articleList) {
        super(articleList);
        mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT_TYPE) {
            return createContentView(parent);
        } else {
            return createFootView(parent);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ContentViewHolder) {
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
    }

    @Override
    public int getItemCount() {
        return getListItemCount() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getListItemCount()) {
            return FOOT_TYPE;
        } else {
            return CONTENT_TYPE;
        }
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
    private ContentViewHolder createContentView(ViewGroup parent) {
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
     *  辅助方法，生成尾布局
     * @return 尾布局的ViewHolder
     */
    private FootViewHolder createFootView(ViewGroup parent) {
        // 创建尾布局
        mFootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_footer_view, parent, false);
        // 默认隐藏尾布局
        mFootView.setVisibility(View.GONE);
        return new FootViewHolder(mFootView);
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
