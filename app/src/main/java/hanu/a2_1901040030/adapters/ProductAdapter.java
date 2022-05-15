package hanu.a2_1901040030.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import hanu.a2_1901040030.R;
import hanu.a2_1901040030.models.Product;
import hanu.a2_1901040030.db.ProductManager;

public class ProductAdapter  extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private final Context context;
    private List<Product> listAllProductByAPI;
    ProductManager productAdapterManager;

    public ProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Product> listAllProductByAPI) {
        this.listAllProductByAPI = listAllProductByAPI;
    }
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resources = R.layout.product_item_view;
        return new ProductViewHolder(LayoutInflater.from(parent.getContext()).inflate(resources, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        if (listAllProductByAPI.get(position) != null)
            holder.bind(listAllProductByAPI.get(position), position);
    }

    @Override
    public int getItemCount() {
        if (listAllProductByAPI == null) return 0;
        return listAllProductByAPI.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imageViewImageProduct;
        private final TextView txtViewProductName, txtViewProductPrice;
        private final ImageButton imageButtonAddToCart;


        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewImageProduct = itemView.findViewById(R.id.imageViewImageProduct);
            txtViewProductName = itemView.findViewById(R.id.txtViewProductName);
            txtViewProductPrice = itemView.findViewById(R.id.txtViewProductPrice);
            imageButtonAddToCart = itemView.findViewById(R.id.imageButtonAddToCart);

        }
        @SuppressLint("NotifyDataSetChanged")
        public void bind(Product product, int position) {
            txtViewProductName.setText(product.getName());
            txtViewProductPrice.setText(String.format("%s",CartAdapter.PRICE_FORMAT_PATTERN.format(product.getUnitPrice())+"₫ "));

            // load product image
            ThumbnailProductLoader loadThumbnailProduct = new ThumbnailProductLoader();
            loadThumbnailProduct.execute(product.getThumbnail());

            imageButtonAddToCart.setOnClickListener(view -> {
                productAdapterManager = ProductManager.getInstance(context);
                AtomicBoolean wasAdded = new AtomicBoolean(false);

                List<Product> showAllProducts = productAdapterManager.allProducts();
                showAllProducts.forEach(eachProduct -> {
                    if (product.getId() == eachProduct.getId()) {
                        eachProduct.setQuantity(eachProduct.getQuantity() + 1);
                        productAdapterManager.updateProduct(eachProduct.getQuantity(), eachProduct.getId());
                        wasAdded.set(true);
                        String notificationExist = "The product has been added to the shopping cart before!";
                        Toast.makeText(context, notificationExist, Toast.LENGTH_SHORT).show();
                    }
                });
                if (!wasAdded.get()) {
                    product.setQuantity(1L);
                    long quantityProduct = product.getQuantity();
                    productAdapterManager.addProduct(new Product(product.getId(), product.getThumbnail(),
                            product.getName(), product.getUnitPrice(), quantityProduct));
                    String notificationSuccess = "Add to cart successfully!";
                    Toast.makeText(context, notificationSuccess, Toast.LENGTH_SHORT).show();
                }
                notifyDataSetChanged();
            });
        }

        @SuppressLint("StaticFieldLeak")
        public class ThumbnailProductLoader extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    if(e instanceof MalformedURLException) e.printStackTrace();
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imageViewImageProduct.setImageBitmap(bitmap);
            }
        }
    }
}
