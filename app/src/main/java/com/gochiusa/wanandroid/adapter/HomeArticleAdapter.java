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

import java.util.Collection;
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

    /**
     * 尾布局
     */
    private View mFootView;
    private Context mContext;



    public HomeArticleAdapter(@Nullable Context context, List<Article> articleList) {
        mArticleList = articleList;
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
     * 获取尾布局
     * @return 列表末端的尾布局
     */
    public View getFootView() {
        return mFootView;
    }

    /**
     * 将外部集合的数据添加到数据源中，并且通知数据源更新
     * @param collection 需要添加的数据的集合
     */
    public void addAll(Collection<? extends Article> collection) {
        mArticleList.addAll(collection);
        notifyDataSetChanged();
    }

    /**
     * 移除数据源中所有的数据
     */
    public void removeAll() {
        mArticleList.clear();
        notifyDataSetChanged();
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
                Article article = mArticleList.get(viewHolder.getAdapterPosition());
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
