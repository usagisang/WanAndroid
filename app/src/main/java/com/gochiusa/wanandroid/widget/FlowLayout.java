package com.gochiusa.wanandroid.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;


import java.util.ArrayList;
import java.util.List;

/**
 *  自定义流式布局
 */
public class FlowLayout extends ViewGroup {
    /**
     *  缓存每一行的View，类似于二重数组
     */
    private List<List<View>> mLineCacheList = new ArrayList<>();

    /**
     *  缓存每一行的高度
     */
    private List<Integer> mLineHeightList = new ArrayList<>();

    private Adapter mAdapter;


    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 清空缓存集合的数据
        mLineCacheList.clear();
        mLineHeightList.clear();

        // 预先创建第0行的列表
        List<View> eachLineList = new ArrayList<>();

        // 获得此ViewGroup上级容器为其推荐的宽和高，以及计算模式
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        // 子View个数
        int childCount = getChildCount();
        // 在wrap_content模式下，记录最大宽度和累计高度
        int totalHeight = 0;
        // 缓存每一行的子View累计宽度和子View最大宽度
        int lineWidth = 0;
        int lineMaxHeight = 0;
        // 循环测量子View
        for (int i = 0;i < childCount;i ++) {
            View childView = getChildAt(i);
            // 如果为GONE状态，直接无视
            if (childView.getVisibility() == View.GONE) {
                continue;
            }
            // 先测量子View的宽高信息
            measureChild(childView, widthMeasureSpec, heightMeasureSpec);
            // 获取子元素的属性，以此获取Margin信息
            MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
            // 添加了Margin之后，一个子View实际占用的宽高
            int childWidth = childView.getMeasuredWidth() + params.leftMargin + params.rightMargin;
            int childHeight = childView.getMeasuredHeight() + params.topMargin + params.bottomMargin;
            // 判断是否可以换行，不换行则累计宽度并寻找最大高度，换行则寻找最大宽度并累计总高度
            if (turnNewLine(lineWidth, childWidth, sizeWidth)) {
                // 缓存这一行的最大高度
                mLineHeightList.add(lineMaxHeight);

                // 将当前的行，储存进入缓存
                mLineCacheList.add(eachLineList);
                // 创建新的一行的View的集合
                eachLineList = new ArrayList<>();
                // 将这个View加入到新的一行
                eachLineList.add(childView);

                // 进行高度累积
                totalHeight += lineMaxHeight;

                // 将子View的宽度、高度赋值到下一行
                lineWidth = childWidth;
                lineMaxHeight = childHeight;
            } else {
                // 添加View进入行列表
                eachLineList.add(childView);

                // 累积行宽度
                lineWidth += childWidth;
                // 比较行的最大的高度
                lineMaxHeight = Math.max(childHeight, lineMaxHeight);
            }
            // 对最后一个元素的补正，避免没有被算入参数
            if (i == childCount - 1) {
                // 不管换不换行，都需要累加高度
                totalHeight += childHeight;

                // 将最后没有被缓存的一行缓存
                mLineCacheList.add(eachLineList);
                // 缓存最后没有被缓存的一行
                mLineHeightList.add(lineMaxHeight);
            }
        }
        // 设置这个ViewGroup最终的宽高信息
        if (heightMode == MeasureSpec.EXACTLY) {
            totalHeight = sizeHeight;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            totalHeight = Math.min(
                    sizeHeight, totalHeight + getPaddingTop() + getPaddingBottom());
        }
        setMeasuredDimension(sizeWidth, totalHeight);
    }

    /**
     *  判断是否需要换行
     */
    private boolean turnNewLine(int totalW, int childW, int maxW) {
        return (totalW + childW) > (maxW - getPaddingLeft() - getPaddingRight());
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int paddingLeft = getPaddingLeft();

        // 当前的画笔定位坐标，其他子View的定位会基于这个坐标，获取其参数进行调整
        int paintLeft = paddingLeft;
        int paintTop = getPaddingTop();

        // 遍历子View
        for (int i = 0;i < mLineCacheList.size();i ++) {
            for (View childView : mLineCacheList.get(i)) {
                MarginLayoutParams params = (MarginLayoutParams) childView.getLayoutParams();
                paintLeft += params.leftMargin;
                // 根据参数定位子View
                childView.layout(paintLeft, paintTop + params.topMargin,
                        paintLeft + childView.getMeasuredWidth(),
                        paintTop + childView.getMeasuredHeight());
                // 再更新Left的参数
                paintLeft += (childView.getMeasuredWidth() + params.rightMargin);
            }
            // 一行的View定位结束后，重置横坐标，累加竖坐标
            paintLeft = paddingLeft;
            paintTop += mLineHeightList.get(i);
        }
    }

    public void setAdapter(Adapter adapter) {
        this.removeAllViews();
        mAdapter = adapter;
        for (int i = 0; i < adapter.getItemCount(); i ++) {
            // 子View添加到ViewGroup
            this.addView(createView(i, adapter));
        }
    }

    /**
     *  通知适配器的绑定数据已经被更新
     */
    public void notifyDataSetChange() {
        synchronized (this) {
            if (mAdapter != null) {
                setAdapter(mAdapter);
            }
        }
    }

    /**
     *  通知有一个新的子项View需要插入到ViewGroup
     * @param position  插入的子项的位置
     */
    public void notifyItemInsert(int position) {
        synchronized (this) {
            if (mAdapter != null) {
                this.addView(createView(position, mAdapter), position);
            }
        }
    }

    /**
     *  创建指定位置的View和ViewHolder
     * @param position 指定位置的下标
     * @param adapter 创建View需要的适配器
     * @return 创建完毕的View
     */
    private View createView(int position, @NonNull Adapter adapter) {
        // 创建ViewHolder
        ViewHolder viewHolder = adapter.onCreateViewHolder(
                this, adapter.getItemViewType(position));
        // 绑定数据
        adapter.onBindViewHolder(viewHolder, position);
        return viewHolder.itemView;
    }

    public abstract static class Adapter<VH extends ViewHolder> {
        public abstract VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType);
        public abstract void onBindViewHolder(@NonNull VH holder, int position);
        public abstract int getItemCount();
        public int getItemViewType(int position) {
            return 0;
        }
    }

    public abstract static class ViewHolder {
        public final View itemView;
        public ViewHolder(@NonNull View itemView) {
            this.itemView = itemView;
        }
    }
}
