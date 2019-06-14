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
import com.dd.wanandroid.entity.Tag;
import com.dd.wanandroid.listener.OnItemClickListener;
import com.dd.wanandroid.ui.view.TagView;

import java.util.List;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleHolder> {

    private static final String TAG = "ArticleAdapter";

    private List<Article> articles;

    private static OnItemClickListener sListener;

    private static int topSize = 0;

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
        holder.tvChapter.setText(holder.tvTag.getContext().getString(R.string.app_tag, article.getSuperChapterName(), article.getChapterName()));
        holder.tvDate.setText(article.getNiceDate());
        Log.d(TAG, "onBindViewHolder: topSize=" + topSize + ", position=" + position);
        holder.tvTop.setVisibility(position < topSize ? View.VISIBLE : View.GONE);
        holder.tvNew.setVisibility(article.isFresh() ? View.VISIBLE : View.GONE);
        holder.ivCollection.setVisibility(article.isCollect() ? View.VISIBLE : View.GONE);
        holder.tvTag.setVisibility(View.GONE);
        if (article.getTags() != null && article.getTags().size() > 0) {
            Tag tag = article.getTags().get(0);
            if (tag != null) {
                holder.tvTag.setTagText(tag.getName());
                holder.tvTag.setVisibility(View.VISIBLE);
            }
        }
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

    public void setArticles(List<Article> articles, int topSize) {
        if (articles != null) {
            Log.d(TAG, "setArticles: topSize is " + topSize );
            this.topSize = topSize;
            this.articles.clear();
            this.articles.addAll(articles);
            notifyDataSetChanged();
        }
    }


    class ArticleHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;

        TextView tvAuthor;

        TextView tvChapter;

        TagView tvTag;

        TextView tvTop;

        TextView tvNew;

        TextView tvDate;

        ImageView ivCollection;

        ArticleHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvAuthor = itemView.findViewById(R.id.tv_author);
            tvChapter = itemView.findViewById(R.id.tv_chapter);
            tvTag = itemView.findViewById(R.id.tv_tag);
            tvTop = itemView.findViewById(R.id.tv_top);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvNew = itemView.findViewById(R.id.tv_new);
            ivCollection = itemView.findViewById(R.id.iv_collect);
        }
    }
}
