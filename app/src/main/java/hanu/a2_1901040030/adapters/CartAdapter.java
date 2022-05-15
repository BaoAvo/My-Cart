package hanu.a2_1901040030.adapters;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import java.text.DecimalFormat;
import java.util.List;

import hanu.a2_1901040030.R;
import hanu.a2_1901040030.models.Product;
import hanu.a2_1901040030.fragments.CartFragment;
import hanu.a2_1901040030.db.ProductManager;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private final Context context;

    private List<Product> listAllProductAddToCart;

    ProductManager managerCartItem;
    public static final DecimalFormat PRICE_FORMAT_PATTERN = new DecimalFormat("###,###,###");

    public CartAdapter(Context context) {
        this.context = context;
    }
    public void setData(List<Product> listAllProductAddToCart){
        this.listAllProductAddToCart = listAllProductAddToCart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int resources = R.layout.cart_item_view;
        return new CartViewHolder(LayoutInflater.from(parent.getContext()).inflate(resources, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if(listAllProductAddToCart.get(position) == null){
            return;
        }
        holder.bind(listAllProductAddToCart.get(position), position);
    }

    @Override
    public int getItemCount() {
        if(listAllProductAddToCart == null)  return 0;
        return listAllProductAddToCart.size();
    }

    public class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView imgThumbnailCartItemView, ivAddQuantityOfProduct, ivSubtractQuantityOfProduct;
        TextView tvNameCartItemView, tvPriceCartItemViewOfProduct, tvCurrentQuantityProduct, tvSumPriceCartItemViewByQuantity;

        public CartViewHolder(@NonNull View cartItemView) {
            super(cartItemView);
            imgThumbnailCartItemView = cartItemView.findViewById(R.id.imgThumbnailCartItemView);
            ivAddQuantityOfProduct = cartItemView.findViewById(R.id.ivAddQuantityOfProduct);
            ivSubtractQuantityOfProduct = cartItemView.findViewById(R.id.ivSubtractQuantityOfProduct);
            tvNameCartItemView = cartItemView.findViewById(R.id.tvNameCartItemView);
            tvPriceCartItemViewOfProduct = cartItemView.findViewById(R.id.tvPriceCartItemViewOfProduct);
            tvCurrentQuantityProduct = cartItemView.findViewById(R.id.tvCurrentQuantityProduct);
            tvSumPriceCartItemViewByQuantity = cartItemView.findViewById(R.id.tvSumPriceCartItemViewByQuantity);
        }
        @SuppressLint("NotifyDataSetChanged")
        public void bind(Product productCartItemView, int position){
            managerCartItem = ProductManager.getInstance(context);
            ImageLoader imageLoader = new ImageLoader();
            imageLoader.execute(productCartItemView.getThumbnail());
            tvNameCartItemView.setText(productCartItemView.getName());
            tvPriceCartItemViewOfProduct.setText(String.format("%s",PRICE_FORMAT_PATTERN.format(productCartItemView.getUnitPrice()) + "₫ "));
            tvCurrentQuantityProduct.setText(String.valueOf(productCartItemView.getQuantity()));
            long totalPriceAProductByQuantity = productCartItemView.getQuantity() * productCartItemView.getUnitPrice();
            tvSumPriceCartItemViewByQuantity.setText(String.format("%s",PRICE_FORMAT_PATTERN.format(totalPriceAProductByQuantity) + "₫ " ));

            // btn Add & Remove
            ivAddQuantityOfProduct.setOnClickListener(view -> {
                productCartItemView.setQuantity(productCartItemView.getQuantity() + 1);
                managerCartItem.updateProduct(productCartItemView.getQuantity(), productCartItemView.getId());
                // set Total price
                long totalPrice = listAllProductAddToCart.stream().mapToLong(productAddToCart ->
                        productAddToCart.getUnitPrice() * productAddToCart.getQuantity()).sum();
                CartFragment.totalPrice(totalPrice);
                notifyDataSetChanged();
            });
            ivSubtractQuantityOfProduct.setOnClickListener(view -> {
                if(productCartItemView.getQuantity() != 1){
                    productCartItemView.setQuantity(productCartItemView.getQuantity() - 1);
                    managerCartItem.updateProduct(productCartItemView.getQuantity(), productCartItemView.getId());
                    long totalPrice = listAllProductAddToCart.stream().mapToLong(productAddToCart ->
                            productAddToCart.getUnitPrice() * productAddToCart.getQuantity()).sum();
                    CartFragment.totalPrice(totalPrice);
                    notifyDataSetChanged();
                }
                else {
                    AlertDialog.Builder notificationAlert = new AlertDialog.Builder(context);
                    notificationAlert.setMessage("Are you sure you want to remove this product?");

                    notificationAlert.setPositiveButton("Yes", (dialogInterface, i) -> {
                        managerCartItem.deleteProduct(productCartItemView.getId());
                        listAllProductAddToCart.remove(productCartItemView);
                        long totalPrice = listAllProductAddToCart.stream().mapToLong(productAddToCart ->
                                productAddToCart.getUnitPrice() * productAddToCart.getQuantity()).sum();
                        CartFragment.totalPrice(totalPrice);
                        notifyDataSetChanged();
                        Toast.makeText(context, "Removed successfully!", Toast.LENGTH_SHORT).show();
                    });
                    notificationAlert.setNegativeButton("No", (dialogInterface, i) -> dialogInterface.cancel());
                    notificationAlert.show();
                }
                notifyDataSetChanged();
            });
        }
        @SuppressLint("StaticFieldLeak")
        public class ImageLoader extends AsyncTask<String, Void, Bitmap> {
            @Override
            protected Bitmap doInBackground(String... strings) {
                Bitmap bitmap = null;
                try {
                    URL url = new URL(strings[0]);
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                } catch (IOException e) {
                    if (e instanceof MalformedURLException)
                    {
                        e.printStackTrace();
                    }
                    e.printStackTrace();
                }
                return bitmap;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                super.onPostExecute(bitmap);
                imgThumbnailCartItemView.setImageBitmap(bitmap);
            }
        }
    }

}
