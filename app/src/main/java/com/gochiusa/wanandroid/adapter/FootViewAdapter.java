package com.gochiusa.wanandroid.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gochiusa.wanandroid.R;

import java.util.List;

/**
 *  一个额外包含了尾布局的适配器
 *  继承自{@code ListAdapter}，使用列表集合来管理数据源
 * @param <T> 需要存放的数据的类型
 * @param <V> ViewHolder的类型，必须继承自{@code FootViewAdapter.NormalViewHolder}
 */
public abstract class FootViewAdapter<T, V extends FootViewAdapter.NormalViewHolder>
        extends ListAdapter<T, FootViewAdapter.NormalViewHolder> {

    /**
     *  子View的类型为内容类型
     */
    static int CONTENT_TYPE = 0;

    /**
     * 子View的类型为尾布局类型
     */
    static int FOOT_TYPE = 1;

    /**
     * 尾布局
     */
    View mFootView;

    public FootViewAdapter(List<T> list) {
        super(list);
    }

    @NonNull
    @Override
    public NormalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == CONTENT_TYPE) {
            return createContentView(parent);
        } else {
            return createFootView(parent);
        }
    }

    /**
     *  生成尾布局
     * @return 尾布局的ViewHolder
     */
    protected NormalViewHolder createFootView(ViewGroup parent) {
        // 创建尾布局
        mFootView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_list_footer_view, parent, false);
        // 默认隐藏尾布局
        mFootView.setVisibility(View.GONE);
        return new FootViewHolder(mFootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NormalViewHolder holder, int position) {
        if (holder.getType() == FOOT_TYPE) {
            onBindFootViewHolder(holder, position);
        } else {
            onBindContentViewHolder(holder, position);
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

    /**
     *  生成携带普通内容的View的ViewHolder，需要子类实现
     */
    abstract NormalViewHolder createContentView(ViewGroup parent);

    /**
     *   需要子类实现，为普通内容的View绑定数据显示的相关操作
     * @param holder 待绑定数据的ViewHolder
     * @param position ViewHolder在列表中的位置
     */
    abstract void onBindContentViewHolder(@NonNull NormalViewHolder holder, int position);

    /**
     *  处理尾布局，默认什么也不做
     */
    void onBindFootViewHolder(@NonNull NormalViewHolder holder, int position) {}

    public void hideFootView() {
        mFootView.setVisibility(View.GONE);
    }

    public void showFootView() {
        mFootView.setVisibility(View.VISIBLE);
    }

    /**
     *  尾布局和普通布局的父类
     */
    public static class NormalViewHolder extends RecyclerView.ViewHolder {
        /**
         *  辨别ViewHolder的类型的变量
         */
        private int type;
        public NormalViewHolder(@NonNull View itemView, int viewHolderType) {
            super(itemView);
            this.type = viewHolderType;
        }
        public int getType() {
            return type;
        }
    }


    /**
     * 尾布局的ViewHolder
     */
    protected static final class FootViewHolder extends NormalViewHolder {
        View footView;
        FootViewHolder(@NonNull View itemView) {
            super(itemView, FOOT_TYPE);
            footView = itemView;
        }
    }
}
