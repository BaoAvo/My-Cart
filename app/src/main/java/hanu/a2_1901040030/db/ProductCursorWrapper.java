package hanu.a2_1901040030.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.List;

import hanu.a2_1901040030.models.Product;

public class ProductCursorWrapper extends CursorWrapper {
    public ProductCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public List<Product> getAllProducts(){
        List<Product> listAllProduct = new ArrayList<>();
        moveToFirst();
        while (!isAfterLast()){
            long id = getLong(0);
            String name = getString(1);
            String thumbnail = getString(2);
            long unitPrice = getLong(3);
            long quantity = getLong(4);
            listAllProduct.add(new Product(id, thumbnail, name, unitPrice, quantity));
            moveToNext();
        }
        return listAllProduct;
    }
}
