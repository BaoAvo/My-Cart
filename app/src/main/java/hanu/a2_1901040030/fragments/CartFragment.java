package hanu.a2_1901040030.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import hanu.a2_1901040030.R;
import hanu.a2_1901040030.adapters.CartAdapter;
import hanu.a2_1901040030.models.Product;
import hanu.a2_1901040030.db.ProductManager;

public class CartFragment extends Fragment {
    List<Product> listAllProductAddToCart;
    @SuppressLint("StaticFieldLeak")
    public static TextView txtViewTotalPriceAllCart;

    @SuppressLint("NotifyDataSetChanged")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cart_activity, container, false);

        RecyclerView recycleViewAllCartAdded = view.findViewById(R.id.recycleViewAllCartAdded);
        txtViewTotalPriceAllCart = view.findViewById(R.id.txtViewTotalPriceAllCart);
        recycleViewAllCartAdded.setLayoutManager(new LinearLayoutManager(getActivity()));

        ProductManager managerCartAdded = ProductManager.getInstance(getActivity());
        listAllProductAddToCart = managerCartAdded.allProducts();
        CartAdapter cartViewItemAdapter = new CartAdapter(getActivity());

        long totalPriceByQuantity = listAllProductAddToCart.stream().mapToLong(productAddToCart ->
                productAddToCart.getUnitPrice() * productAddToCart.getQuantity()).sum();
        totalPrice(totalPriceByQuantity);

        cartViewItemAdapter.setData(listAllProductAddToCart);
        recycleViewAllCartAdded.setAdapter(cartViewItemAdapter);
        cartViewItemAdapter.notifyDataSetChanged();
        return view;
    }
    public static void totalPrice(long totalPriceByQuantity){
        String totalPriceFormat = String.format("%s",CartAdapter.PRICE_FORMAT_PATTERN.format(totalPriceByQuantity)+"₫ ");
        txtViewTotalPriceAllCart.setText(totalPriceFormat);
    }

}