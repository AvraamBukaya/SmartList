package com.avraam.smartlist.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.avraam.smartlist.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class SearchAdpter extends RecyclerView.Adapter <SearchAdpter.SearchViewHolder> {

    Context context;
    private ArrayList<String> products;
    private ArrayList<String> barcodes;
    private ArrayList<String> prices;

    public SearchAdpter(Context context, ArrayList<String> products, ArrayList<String> barcodes,ArrayList<String> prices) {
        this.context = context;
        this.products = products;
        this.barcodes = barcodes;
        this.prices = prices;
    }

    class SearchViewHolder extends RecyclerView.ViewHolder{

        private TextView nProductName;
        private TextView nBarcodeName;
        private TextView nPriceName;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            nProductName = itemView.findViewById(R.id.product_name_search);
            nBarcodeName = itemView.findViewById(R.id.product_barcode_search);
            nPriceName = itemView.findViewById(R.id.product_price_search);
        }
    }

    @NonNull
    @Override
    public SearchAdpter.SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        holder.nProductName.setText(products.get(position));
        holder.nBarcodeName.setText(barcodes.get(position));
        holder.nPriceName.setText(prices.get(position));

    }


    @Override
    public int getItemCount() {
        return products.size();
    }
}
