package hanu.a2_1901040030;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import hanu.a2_1901040030.fragments.CartFragment;
import hanu.a2_1901040030.fragments.ProductFragment;

public class MainActivity extends AppCompatActivity {
    FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        Fragment productFragment = new ProductFragment();

        fragmentManager.beginTransaction()
                .add(R.id.mainFrameLayout, productFragment)
                .addToBackStack("fragment_product")
                .commit();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_item, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int resources = R.id.mainFrameLayout;

        switch (item.getItemId()){
            case R.id.btnViewAllAddToCart:
                Fragment addToCartFragment = new CartFragment();
                fragmentManager.beginTransaction()
                        .replace(resources, addToCartFragment)
                        .commit();
                break;
            case R.id.btnBackMainScreen:
                Fragment mainScreenFragment = new ProductFragment();
                fragmentManager.beginTransaction()
                        .replace(resources, mainScreenFragment)
                        .commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int resources = R.id.mainFrameLayout;
        Fragment productFragment = new ProductFragment();
        fragmentManager.beginTransaction()
                .replace(resources, productFragment)
                .commit();
    }
}