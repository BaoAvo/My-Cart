package hanu.a2_1901040030.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import hanu.a2_1901040030.R;
import hanu.a2_1901040030.adapters.ProductAdapter;
import hanu.a2_1901040030.models.Product;

public class ProductFragment extends Fragment implements View.OnClickListener{
    private ProductAdapter productAdapter;
    private ArrayList<Product> productList;
    private static final String CART_API = "https://mpr-cart-api.herokuapp.com/products";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.product_fragment, container, false);
        RecyclerView recycleViewProduct = view.findViewById(R.id.recycleViewProduct);
        EditText editTextSearch = view.findViewById(R.id.editTextSearch);

        APILoader api = new APILoader();
        api.execute(CART_API);

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(getActivity());
        productAdapter.setData(productList);

        GridLayoutManager grid = new GridLayoutManager(getActivity(), 2);
        recycleViewProduct.setLayoutManager(grid);
        recycleViewProduct.setAdapter(productAdapter);

        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void afterTextChanged(Editable editable) {
                searchByName(editable.toString().trim());
            }
        });

        return view;
    }

    @Override
    public void onClick(View view) {

    }
    @SuppressLint("NotifyDataSetChanged")
    public void searchByName(String name){
        ArrayList<Product> searchedProduct = new ArrayList<>();
        productList.forEach(product -> {
            if(product.getName().toLowerCase().contains(name.toLowerCase())){
                searchedProduct.add(product);
            }
        });
        productAdapter.setData(searchedProduct);
        productAdapter.notifyDataSetChanged();
    }

    @SuppressLint("StaticFieldLeak")
    public class APILoader extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder stringBuilder = new StringBuilder();
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                InputStream is = urlConnection.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);
                String character ;
                while ((character = br.readLine()) != null){
                    stringBuilder.append(character).append("\n");
                }

            } catch (IOException e) {
                if(e instanceof MalformedURLException) e.printStackTrace();
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }

        @SuppressLint("NotifyDataSetChanged")
        @Override
        protected void onPostExecute(String data) {
            super.onPostExecute(data);
            if(data == null){
                return;
            }
            try {
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++){
                    JSONObject jsonProduct = array.getJSONObject(i);
                    int id = jsonProduct.getInt("id");
                    String thumbnail = jsonProduct.getString("thumbnail");
                    String name = jsonProduct.getString("name");
                    int unitPrice = jsonProduct.getInt("unitPrice");
                    productList.add(new Product(id, thumbnail, name, unitPrice));
                }
                productAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}