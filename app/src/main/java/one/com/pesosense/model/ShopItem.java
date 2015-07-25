package one.com.pesosense.model;

/**
 * Created by mykelneds on 7/25/15.
 */
public class ShopItem {

    /**
     * int product_id,cat_id,subcat_id,product_cost,product_price,product_quantity;
     * String product_name,product_desc,product_image;
     */

    int id, price, rating;
    String name, description, image;

    public ShopItem(int id, int price, int rating, String name, String description, String image) {
        this.id = id;
        this.price = price;
        this.rating = rating;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public int getRating() {
        return rating;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }
}
