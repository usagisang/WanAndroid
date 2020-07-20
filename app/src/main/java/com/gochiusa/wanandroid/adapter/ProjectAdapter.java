package com.gochiusa.wanandroid.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.gochiusa.wanandroid.R;
import com.gochiusa.wanandroid.entity.Project;
import com.gochiusa.wanandroid.tasks.web.WebViewActivity;
import com.gochiusa.wanandroid.util.loader.ImageLoader;

import java.util.List;


public class ProjectAdapter extends FootViewAdapter<Project, ProjectAdapter.ProjectViewHolder> {

    /**
     * 上下文
     */
    private Context mContext;

    public ProjectAdapter(List<Project> list, Context context) {
        super(list);
        mContext = context;
    }

    @Override
    NormalViewHolder createContentView(ViewGroup parent) {
        // 创建普通的子项
        View itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_main_home_page, parent, false);
        // 创建ViewHolder
        ProjectViewHolder viewHolder = new ProjectViewHolder(itemView);
        // 设置子项点击事件
        itemView.setOnClickListener((view) -> {
            // 仅当context不为null时进行跳转
            if (mContext != null) {
                Project project = getItem(viewHolder.getAdapterPosition());
                WebViewActivity.startThisActivity(mContext, project.getLink());
            }
        });
        return viewHolder;
    }

    @Override
    void onBindContentViewHolder(@NonNull NormalViewHolder holder, int position) {
        // 强制类型转换
        ProjectViewHolder viewHolder = (ProjectViewHolder) holder;
        // 获取数据
        Project project = getItem(position);
        // 将字符串数据展示在子项的控件上
        viewHolder.timeView.setText(project.getNiceDate());
        viewHolder.authorView.setText(project.getAuthor());
        viewHolder.descriptionView.setText(project.getDescription());
        viewHolder.projectTitleView.setText(project.getTitle());
        // 通过工具类发起图片请求
        ImageLoader.with(mContext).load(
                project.getEnvelopePictureLink()).into(viewHolder.imageView);
    }


    static class ProjectViewHolder extends FootViewAdapter.NormalViewHolder {
        ImageView imageView;
        TextView projectTitleView;
        TextView descriptionView;
        TextView authorView;
        TextView timeView;
        public ProjectViewHolder(@NonNull View itemView) {
            super(itemView, CONTENT_TYPE);
            imageView = itemView.findViewById(R.id.iv_project_picture);
            projectTitleView = itemView.findViewById(R.id.tv_project_title);
            descriptionView = itemView.findViewById(R.id.tv_project_desc);
            authorView = itemView.findViewById(R.id.tv_project_author);
            timeView = itemView.findViewById(R.id.tv_project_date);
        }
    }
}
