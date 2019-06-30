package com.dd.wanandroid.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.Tree;
import com.google.android.flexbox.FlexboxLayout;

import java.util.List;

import io.realm.RealmList;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.TreeHolder> {

    private List<Tree> trees;

    private Context context;

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public TreeAdapter(List<Tree> trees, Context context) {
        this.trees = trees;
        this.context = context;
    }

    public void setTrees(List<Tree> trees) {
        if (trees != null) {
            this.trees.clear();
            this.trees.addAll(trees);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public TreeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_tree, parent, false);
        return new TreeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeHolder holder, int position) {
        final int index = position;
        Tree tree = trees.get(position);
        holder.tvTitle.setText(tree.getName());
        RealmList<Tree> children = tree.getChildren();
        initFlexbox(holder.flexboxLayout, children);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onItemClick(index);
                }
            }
        });
    }

    private void initFlexbox(FlexboxLayout flexboxLayout, RealmList<Tree> children) {
        flexboxLayout.removeAllViews();
        flexboxLayout.setFocusableInTouchMode(false);
        for (int i = 0; i < children.size(); i++) {
            Tree tree = children.get(i);
            TextView tagView = (TextView) LayoutInflater.from(flexboxLayout.getContext()).inflate(R.layout.tag_view, flexboxLayout, false);
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) tagView.getLayoutParams();
            lp.rightMargin = 16;
            lp.bottomMargin = 16;
            tagView.setLayoutParams(lp);
            if (tree != null) {
                tagView.setText(tree.getName());
                flexboxLayout.addView(tagView);
            }
        }
    }

    @Override
    public int getItemCount() {
        return trees == null ? 0 : trees.size();
    }

    class TreeHolder extends RecyclerView.ViewHolder {

        ImageView ivArrow;
        TextView tvTitle;
        FlexboxLayout flexboxLayout;

        TreeHolder(View itemView) {
            super(itemView);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
            tvTitle = itemView.findViewById(R.id.tv_title);
            flexboxLayout = itemView.findViewById(R.id.flexbox_layout);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
