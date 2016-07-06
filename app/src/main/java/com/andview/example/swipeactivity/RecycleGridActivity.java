package com.andview.example.swipeactivity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andview.example.R;
import com.andview.example.moduler.User;
import com.tubb.smrv.SwipeHorizontalMenuLayout;

import java.util.List;

public class RecycleGridActivity extends AbstractRvActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.addItemDecoration(new GridSpaceItemDecoration(2, 3));
    }

    @Override
    protected AbstractRvAdapter createAppAdapter(AbstractRvActivity baseRvActivity, List<User> users) {
        return new GridRvAppAdapter(baseRvActivity, users);
    }

    private class GridRvAppAdapter extends AbstractRvAdapter {

        public GridRvAppAdapter(Context context, List<User> users){
            super(context, users);
        }

        @Override
        protected RecyclerView.ViewHolder createViewHolder(View itemView) {
            return new GridRvViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder vh, final int position) {
            super.onBindViewHolder(vh, position);
            final GridRvViewHolder myViewHolder = (GridRvViewHolder)vh;
            final SwipeHorizontalMenuLayout itemView = (SwipeHorizontalMenuLayout) myViewHolder.itemView;
            final User user = users.get(position);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,
                            "Hi " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.btGood.setVisibility(View.GONE);
            myViewHolder.getBtOpen().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,
                            "Open " + user.getUserName(), Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.getBtDelete().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // must close normal
                    itemView.smoothCloseMenu();
                    users.remove(vh.getAdapterPosition());
                    mAdapter.notifyItemRemoved(vh.getAdapterPosition());
                }
            });
            myViewHolder.btLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "Left click", Toast.LENGTH_SHORT).show();
                }
            });
            myViewHolder.getTvName().setText(user.getUserName());
            myViewHolder.tvSwipeEnable.setVisibility(View.GONE);
        }
    }

    public static class GridRvViewHolder extends AbstractRvActivity.AbstractViewHolder {
        TextView tvSwipeEnable;
        View btGood;
        View btLeft;
        public GridRvViewHolder(View itemView) {
            super(itemView);
            tvSwipeEnable = (TextView) itemView.findViewById(R.id.tvSwipeEnable);
            btGood = itemView.findViewById(R.id.btGood);
            btLeft = itemView.findViewById(R.id.btLeft);
        }
    }

}
