package hanu.a2_1901040030.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import java.util.List;

import hanu.a2_1901040030.models.Product;

public class ProductManager {
    private static ProductManager instance;
    private final SQLiteDatabase sqlDatabase;

    public static ProductManager getInstance(Context context){
        if(instance == null){
            instance = new ProductManager(context);
        }
        return instance;
    }

    public ProductManager(Context context){
        ProductDbHelper productDbHelper = new ProductDbHelper(context);
        sqlDatabase = productDbHelper.getWritableDatabase();
    }

    // get all Products
    public List<Product> allProducts(){
        String sql = "SELECT * FROM products";
        Cursor queryAllProductsFromProductsTable = sqlDatabase.rawQuery(sql, null);
        ProductCursorWrapper wrapper = new ProductCursorWrapper(queryAllProductsFromProductsTable);
        List<Product> listAllProduct = wrapper.getAllProducts();
        queryAllProductsFromProductsTable.close();
        return listAllProduct;
    }
    // add
    public boolean addProduct(Product product){
        SQLiteStatement sqltm =
                sqlDatabase.compileStatement( "INSERT INTO products (id, thumbnail, name, unitPrice, quantity) " +
                        "VALUES (?, ?, ?, ?, ?)");
        sqltm.bindLong(1, product.getId());
        sqltm.bindString(2, product.getThumbnail());
        sqltm.bindString(3, product.getName());
        sqltm.bindLong(4, product.getUnitPrice());
        sqltm.bindDouble(5, product.getQuantity());
        long id = sqltm.executeInsert();
        if(id > 0){
            product.setId(id);
            return true;
        }
        return false;
    }

    // update
    public boolean updateProduct(long quantity, long id){
        String query = "UPDATE products SET quantity = '" + quantity + "' WHERE id = '" + id + "'";
        sqlDatabase.execSQL(query);
        return true;
    }

    // delete
    public boolean deleteProduct(long id) {
        int result = sqlDatabase.delete("products", "id = ?", new String[]{ id + "" });
        return result > 0;
    }
}
