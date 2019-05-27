package com.dd.wanandroid.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.Article;
import com.dd.wanandroid.entity.Tree;
import com.dd.wanandroid.ui.view.TagLayout;

import java.util.List;

import io.realm.RealmList;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class TreeAdapter extends RecyclerView.Adapter<TreeAdapter.TreeHolder> {

    private List<Tree> trees;

    public TreeAdapter(List<Tree> trees) {
        this.trees = trees;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tree, parent, false);
        return new TreeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TreeHolder holder, int position) {
        Tree tree = trees.get(position);
        holder.tvTitle.setText(tree.getName());
        RealmList<Tree> children = tree.getChildren();
        holder.tagLayout.addTrees(children);
    }

    @Override
    public int getItemCount() {
        return trees == null ? 0 : trees.size();
    }

    class TreeHolder extends RecyclerView.ViewHolder {

        ImageView ivArrow;
        TextView tvTitle;
        TagLayout tagLayout;

        TreeHolder(View itemView) {
            super(itemView);
            ivArrow = itemView.findViewById(R.id.iv_arrow);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tagLayout = itemView.findViewById(R.id.tl_sub);
        }
    }
}
