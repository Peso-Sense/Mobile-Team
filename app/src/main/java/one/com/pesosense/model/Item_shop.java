package one.com.pesosense.model;

/**
 * Created by mobile2 on 7/9/15.
 */
public class Item_shop {


    int prod_id,user_id;
    String prod_name,prod_desc,prod_image,prod_brand,prod_price,prod_quantity;


    public Item_shop(int prod_id, int user_id, String prod_name, String prod_desc, String prod_price, String prod_brand, String prod_quantity, String prod_image) {
        this.prod_id = prod_id;
        this.user_id = user_id;
        this.prod_name = prod_name;
        this.prod_desc = prod_desc;
        this.prod_price = prod_price;
        this.prod_brand = prod_brand;
        this.prod_quantity = prod_quantity;
        this.prod_image = prod_image;

    }

    public int getProduct_id() {
        return prod_id;
    }

    public int getUser_id() {
        return user_id;
    }


    public String getProd_name() {
        return prod_name;
    }

    public void setProd_name(String prod_name) {
        this.prod_name = prod_name;
    }


    public String getProd_desc() {
        return prod_desc;
    }

    public void setProd_desc(String prod_desc) {
        this.prod_desc = prod_desc;
    }


    public String getProd_price() {
        return prod_price;
    }

    public void setProd_price(String prod_price) {
        this.prod_price = prod_price;
    }

    public String getProd_brand() {
        return prod_brand;
    }

    public void setProd_brand(String prod_brand) {
        this.prod_brand = prod_brand;
    }



    public String getProd_quantity() {
        return prod_quantity;
    }

    public void setProd_quantity(String prod_quantity) {
        this.prod_quantity = prod_quantity;
    }



    public String getProd_image() {
        return prod_image;
    }

    public void setProd_image(String prod_image) {
        this.prod_image = prod_image;
    }


}


















