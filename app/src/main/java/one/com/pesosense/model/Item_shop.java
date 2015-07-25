package one.com.pesosense.model;

/**
 * Created by mobile2 on 7/9/15.
 */
public class Item_shop {


    int product_id,cat_id,subcat_id,product_cost,product_price,product_quantity;
    String product_name,product_desc,product_image;



    public Item_shop(int product_id, int cat_id, int subcat_id, String product_name, String product_desc, int product_cost, int product_price, int product_quantity, String product_image) {
        this.product_id = product_id;
        this.cat_id = cat_id;
        this.subcat_id = subcat_id;
        this.product_name = product_name;
        this.product_desc = product_desc;
        this.product_cost = product_cost;
        this.product_price = product_price;
        this.product_quantity = product_quantity;
        this.product_image = product_image;

    }

    public int getProduct_id() {
        return product_id;
    }

    public int getCat_id() {
        return cat_id;
    }

    public int getSubcat_id() {
        return subcat_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_desc() {
        return product_desc;
    }

    public void setProduct_desc(String product_desc) {
        this.product_desc = product_desc;
    }

    public int getProduct_cost() {
        return product_cost;
    }

    public int getProduct_price() {
        return product_price;
    }

    public int getProduct_quantity() {
        return product_quantity;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }







}
















