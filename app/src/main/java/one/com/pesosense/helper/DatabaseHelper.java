package one.com.pesosense.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by mykelneds on 7/4/15.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static DatabaseHelper mInstance = null;

    private DatabaseHelper(Context mContext) {
        super(mContext, "onex_db", null, 6);
    }

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_feeds(id varchar, type integer, timestamp varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_video(id varchar, profile_pic varchar, message varchar, link varchar, likes integer, comment integer, timestamp varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_image(id varchar, profile_pic varchar, message varchar, link varchar, likes integer, comment integer, timestamp varchar)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_remittances (id integer primary key, date varchar, title varchar, message varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_financial_tips (id integer, type integer, tips_english varchar, tips_tagalog varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_remittances (rem_id integer primary key, " +
                "rem_date varchar, rem_title varchar, rem_message varchar)");


        db.execSQL("CREATE TABLE IF NOT EXISTs tbl_user_info (imagepath varchar, lname varchar, fname varchar, mname varchar, gender varchar, birthday varchar, address varchar, email varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_otop (id integer primary key, province varchar, name varchar, price varchar, product_image varchar, description varchar, ratings int, brand varchar, inventory varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_category (cat_id integer primary key, cat_name varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_subcategory(subcat_id integer primary key, subcat_name varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_products(product_id integer primary key autoincrement, cat_id integer, subcat_id integer, product_name varchar, product_desc varchar, product_cost integer, product_price integer, product_quantity integer, product_image varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_grabbed(grabbed_id integer primary key autoincrement, cat_id integer, subcat_id integer, grabbed_name varchar, grabbed_desc varchar, grabbed_cost integer, grabbed_price integer, grabbed_quantity integer, grabbed_image varchar)");
        populateTips(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS tbl_fb_feeds");
        db.execSQL("DROP TABLE IF EXISTS tbl_fb_video");
        db.execSQL("DROP TABLE IF EXISTS tbl_fb_image");
        db.execSQL("DROP TABLE IF EXISTS tbl_financial_tips");

        db.execSQL("DROP TABLE IF EXISTS tbl_remittances");

        db.execSQL("DROP TABLE IF EXISTS tbl_category");
        db.execSQL("DROP TABLE IF EXISTS tbl_subcategory");
        db.execSQL("DROP TABLE IF EXISTS tbl_products");
        db.execSQL("DROP TABLE IF EXISTS tbl_grabbed");

        db.execSQL("DROP TABLE IF EXISTS tbl_user_info");

        db.execSQL("DROP TABLE IF EXISTS tbl_otop");
        db.execSQL("DROP TABLE IF EXISTS tbl_category");
        db.execSQL("DROP TABLE IF EXISTS tbl_subcategory");
        db.execSQL("DROP TABLE IF EXISTS tbl_products");
        db.execSQL("DROP TABLE IF EXISTS tbl_grabbed");
        onCreate(db);

    }

    public void populateTips(SQLiteDatabase db) {
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (1, 1, 'Keep track of your business monthly expenses.', 'Ilista ang mga buwanang gastusin sa negosyo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (2, 1, 'Separate your personal from business expenses', 'Huwag bayaran ang mga pansariling gastos mula sa kita ng negosyo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (3, 1, 'Buy in wholesale to avail of discounts', 'Mamili ng maramihan para makatipid') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (4, 1, 'Borrow and do not use all your money for capital investment', 'Mangutang sa halip na gamitin ang sariling pera para mamuhunan ') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (5, 1, 'Register and never compromise your business', 'Iparehistro ang iyong negosyo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (6, 1, 'Open a separate bank acount for your business', 'Magbukas ng hiwalay na account sa banko para magnegosyo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (7, 1, 'Limit your customers credit credit to avoid financial loss', 'Limitahan ang pagpapautang sa mga kliyente para hindi malugi') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (8, 1, 'Pay your taxes right', 'Magbayad ng tamang buwis') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (9, 1, 'Participate in business-related trainings ', 'Maghanap ng mga libreng pagsasanay o training sa pamamalakad ng negosyo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (10, 1, 'Invest profit for high yielding bank products', 'Ipamuhunan ang kita ng negosyo sa mga produkto ng bangko na kumikita ng mataas na interes') ");

        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (11, 2, 'Make detailed list of expenses and expenditures', 'Gumawa ng detalyado at maayos na tala ng pangkakagastusan') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (12, 2, 'Prioritize the things that the household needs', 'Unahin muna ang mga mahahalagang bagay o pangangailangan ng pamilya at sa tahanan') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (13, 2, 'Plan ahead for your childrens need', 'Planuhin ng maaga ang kinabukasan ng iyong mga anak') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (14, 2, 'Prepare an emergency fund', 'Maghabda ng sapat na ipon para sa mga biglaang pangangailangan') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (15, 2, 'Involve family members on proper financial management', 'Isama ang buong pamilya sa wastong paggamit ng pananalapi ') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (16, 2, 'Avail health and insurance premiums for your family', 'Kumuha ng health and insurance products para sa iyong pamilya') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (17, 2, 'Look for cheaper ways to spend time with your family', 'Maghanap ng mga murang paraan para maglibang kasama ang pamilya') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (18, 2, 'Celebrating family ocassions simply', 'Magdiwang kasama ang pamilya sa payak na pamamaraan') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (19, 2, 'Learn when to say no', 'Matutong tumanggi o magsabi ng hindi') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (20, 2, 'Lead a frugal life', 'Ang paraang pinagpaguran bigyan ng kahalagahan') ");

        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (21, 3, 'Pay yourself first', 'Unahin ang pagtatabi ng sapat na ipon para sa sarili') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (22, 3, 'Have long-term financial goals', 'Magkaroon ng pang matagalang planong pinansiyal') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (23, 3, 'Prepare for the rainy days', 'Maghanda para sa mga di inaasahang pangyayari') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (24, 3, 'Open a bank account', 'Magbukas ng sariling bank account') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (25, 3, 'Avoid reckless spending during paydays', 'Iwasan ang maluhong paggastos tuwing sweldo') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (26, 3, 'Avoid loans to pay for personal expenses', 'Iwasan mangutang upang ipambayad sa iba pang pagkakautang') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (27, 3, 'Look for other sources of extra income.', 'Maghanap pa ng ibang pagkakakitaan.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (28, 3, 'Know your tax exemption priveleges', 'Alamin ang mga kaukulang tax exemption.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (29, 3, 'Work harder to be promoted', 'Magtrabaho ng maayos at maiigi upang mapromote sa trabaho.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (30, 3, 'Avoid peer-pressure.', 'Huwag basta magpapadala sa impluwensya ng mga kaibigan') ");

        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (31, 4, 'Prepare for your future needs', 'Maghanda para sa hinaharap') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (32, 4, 'Use your retirement funds wisely', 'Maging matalino sa pag-gamit ng retirement funds') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (33, 4, 'Give importance on your health.', 'Pahalagahan ang iyong kalusugan.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (34, 4, 'Involve yourself in fitness activities', 'Lumahok sa mga gawaing pangkalusugan.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (35, 4, 'Involve yourself in in activities related to financial literacy', 'Lumahok sa mga gawaing tungkol sa kaalmang pinansiyal') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (36, 4, 'Limit yourself in getting into risky investments.', 'Limitahan ang sarili sa mga peligrosong pamummuhunan.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (37, 4, 'Settle the disposition of your properties to avoid conflicts.', 'Siguraduhing maayos ang pamamahagi ng iyong mga pamanang ari-arian') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (38, 4, 'Involve your family in making financial decisions', 'Isama ang pamilya sa mga desisyong pinansiyal') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (39, 4, 'Learn when to say no', 'Matutuong tumanggi o magsabi ng hindi') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (40, 4, 'Know, maximize and enjoy the benifits of your senior citizen card.', 'Alamin at pakinabangan ng husto ang benepisyo ng iyong senior citizen card.') ");

        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (41, 5, 'Learn the art of self-control.', 'Matutong kontrolin ang sarili.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (42, 5, 'Prioritize needs over wants.', 'Bigyang prayoridad ang mga bagay na kailangan at importante sa buhay kaysa sa mga luho') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (43, 5, 'Avoid peer-pressure.', 'Huwag basta magpapadala sa impluwensya ng mga kaibigan') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (44, 5, 'Prepare for the rainy days', 'Maghanda ng sapat na ipon para sa mga di inaasahang pangyayari.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (45, 5, 'Maintain a budget and comply with it.', 'Matutong gumawa ng budget at sundin ito.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (46, 5, 'Pay and understand your taxes.', 'Alamin at bayaran ng tamang buwis.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (47, 5, 'Open a bank savings with automatic savings facility.', 'Magbukas ng bank account na may automatic savings facility.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (48, 5, 'Build and maintain a good credit record', 'Magsimula at pangalagaan ang maayos na credit record.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (49, 5, 'Know how to invest wisely', 'Matutong mamuhunan ng tama.') ");
        db.execSQL("INSERT INTO tbl_financial_tips(id, type, tips_english, tips_tagalog) VALUES (50, 5, 'Have long-term financial goals.', 'Magkaroon ng pang-matagalang planong pinansyal.') ");

        /***    ***     ***     ***     ***     ***     ***     ***     ***/
        db.execSQL("INSERT INTO tbl_remittances (rem_id, rem_date, rem_title, rem_message) VALUES (1, '01-01-2015', 'SMART', 'This is a sample content') ");



        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 1, 'Batanes', 'Miniovaheng', '150pesos', " +
                "'http://journeyingjames.com/wp-content/uploads/2011/05/Rice-wine.jpg' , 'Sugarcane Wine' , 4, 'Miniovaheng', '105') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 2, 'Surigao del sur', 'Fashionable neckpieces', '120pesos', " +
                "'http://www.interaksyon.com/lifestyle/assets/2014/10/IMG_1809e.jpg' , 'description' , 3, 'wood', '150') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 3, 'Palawan', 'Palawan wood figurines', '150pesos', " +
                "'http://www.palawan-philippines.com/images/tn_wood-figurines.jpg' , 'description' , 4, 'wood', '200') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 4, 'Palawan', 'Palawan wood carving design', '200pesos', " +
                "'https://retirednoway.files.wordpress.com/2011/02/2011-feb-puerto-princesa-to-nasiduan-27.jpg' , 'description' , 3, 'wood', '120') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 5, 'Ilocos Norte', 'Bagnet', '220 pesos', " +
                "'http://heftyfoodie.com/wp-content/uploads/2014/01/manongs_bagnet_station_cover.jpg' , 'description' , 4, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 6, 'Vigan', 'Vigan empanada 5 pcs', '120 pesos', " +
                "'http://4.bp.blogspot.com/-lt71C9Y9hMA/UOFWt-ruQwI/AAAAAAAAEY0/YUfevSexgAM/s1600/IMAG0094.jpg' , 'description' , 4, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 7, 'Zambales', 'Zambales Jar With Cover', '450 pesos', " +
                "'http://i00.i.aliimg.com/photo/v0/11516384/Zambales_Jar_With_Cover.jpg' , 'description' , 5, 'jars', '120') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 8, 'Pangasinan', 'Lingayens Bagoong', '120 pesos', " +
                "'http://4.bp.blogspot.com/-LY5UhJ20sN8/UbvgR0lFyjI/AAAAAAAADRc/n6NGVhhm9Q4/s320/pangasinantour_JBbagoong+%283%29.jpg' , 'description' , 3, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 9, 'Nueva Ecija', 'Puno’s Ice Cream and Sherbet', '150 pesos', " +
                "'https://jhanellamanabat.files.wordpress.com/2015/03/adsfd.jpg' , 'description' , 3, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 10, 'Bulacan', 'Pastillas', '100 pesos', " +
                "'http://www.bulacan.gov.ph/business/images/themes/pastillas/masthead.jpg' , 'description' , 3, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 11, 'Pampanga', 'Longaniza', '95 pesos', " +
                "'http://static.ensogo.com.ph/assets/deals/16707a26f8b008cb6f6d860d4f4b7591/description1.jpg' , 'Pampangas Best Best Longaniza 500g for only P95 (valued at P121)' , 5, 'food', 'unlimited') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 12, 'Aklan', 'AKlan dress and barong', '2095 pesos', " +
                "'https://s-media-cache-ak0.pinimg.com/736x/0c/6b/42/0c6b4291d434034ba94c11725a5dd28f.jpg' , 'description' , 4, 'Dress', '30') ");

        db.execSQL("INSERT INTO tbl_otop (id, province, name, price, product_image, description, ratings, brand, inventory) VALUES ( 13, 'Capiz', 'Capiz shell lamp', '300 pesos', " +
                "'http://www.retireinthephilippines.info/wp-content/uploads/2012/05/Capiz-Capiz-shell-products.jpg' , 'description' , 4, 'furnitures', '20') ");




    }

}