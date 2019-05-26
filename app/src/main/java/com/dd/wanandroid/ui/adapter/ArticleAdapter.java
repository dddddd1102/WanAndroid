package com.dd.wanandroid.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.Article;
import com.dd.wanandroid.listener.OnItemClickListener;

import java.util.List;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private List<Article> articles;

    private static OnItemClickListener sListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        sListener = listener;
    }

    public ArticleAdapter(List<Article> articles) {
        this.articles = articles;
    }

    @NonNull
    @Override
    public ArticleHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_article, parent, false);
        return new ArticleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleHolder holder, int position) {
        final int index = position;
        Article article = articles.get(position);
        holder.tvTitle.setText(article.getTitle());
        holder.tvAuthor.setText(article.getAuthor());
        holder.tvChapterName.setText(article.getSuperChapterName());
        holder.tvDate.setText(article.getNiceDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sListener != null) {
                    sListener.onItemClick(index);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return articles == null ? 0 : articles.size();
    }

    public void setArticles(List<Article> articles) {
        if (articles != null) {
            this.articles.clear();
            this.articles.addAll(articles);
            notifyDataSetChanged();
        }
    }


    class ArticleHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        TextView tvAuthor;

        TextView tvChapterName;

        TextView tvDate;

        ArticleHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvChapterName = itemView.findViewById(R.id.tv_chapter_name);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
