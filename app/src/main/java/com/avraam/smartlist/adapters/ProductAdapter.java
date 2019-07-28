package com.avraam.smartlist.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.avraam.smartlist.R;
import com.avraam.smartlist.models.Product;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProductAdapter extends FirestoreRecyclerAdapter<Product,ProductAdapter.ProductHolder> {


    public ProductAdapter(@NonNull FirestoreRecyclerOptions<Product> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductAdapter.ProductHolder productHolder, int i, @NonNull Product model) {
        productHolder.textViewTitle.setText(model.getProduct_Name()+"\n");
        productHolder.textViewBarcode.setText("מספר הברקוד: "+model.getBarcode()+"\n");
        productHolder.textViewAddedDate.setText("תאריך הוספה: "+model.getDate_Added());
    }

    @NonNull
    @Override
    public ProductAdapter.ProductHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_product,
                parent, false);
        return new ProductAdapter.ProductHolder(view);
    }


    class ProductHolder extends RecyclerView.ViewHolder{

        TextView textViewTitle;
        TextView textViewBarcode;
        TextView textViewAddedDate;

        public ProductHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_product_title);
            textViewBarcode = itemView.findViewById(R.id.text_view_product_barcode);
            textViewAddedDate = itemView.findViewById(R.id.text_view_product_addedDate);
        }
    }

}
