package com.Advanced.Academy.Mahfazty.adapters;

/*

* Designed and developed by
 * Eslam Mostafa Sayed
 * Amgad Mohamed Attia
 * Ashraf Mahmoud Abdulmaged
 * Amir Hussain Mostafa

as a graduation project for the year of 2017
Advanced Academy
*/

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.Advanced.Academy.Mahfazty.R;
import com.Advanced.Academy.Mahfazty.TransactionListActivity;
import com.Advanced.Academy.Mahfazty.models.Transaction;

import java.util.List;

public class TransactionsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int TYPE_REGULAR = 0;

    private List<Transaction> mItems;
    private Activity activity;

    public TransactionsListAdapter(Activity activity, List<Transaction> items) {
        this.activity = activity;
        mItems = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //create new ViewHolder from layout file to set data in future
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_transaction, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        //setting transaction info
        Transaction item = mItems.get(position);
        viewHolder.mName.setText(item.getCategory());
        viewHolder.mAmount.setText(String.valueOf(item.getAmount()));
        viewHolder.root.setOnLongClickListener(v -> {
            ((TransactionListActivity) activity).toggleSelection(item.getId());
            return false;
        });
    }

    @Override
    public int getItemCount() {
        if (mItems == null) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_REGULAR;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        //viewholder class for expense or income items
        TextView mName;
        TextView mAmount;
        RelativeLayout root;

        public ViewHolder(View view) {
            super(view);
            root = (RelativeLayout) view.findViewById(R.id.root);
            mName = (TextView) view.findViewById(R.id.name);
            mAmount = (TextView) view.findViewById(R.id.amount);
        }
    }
}
