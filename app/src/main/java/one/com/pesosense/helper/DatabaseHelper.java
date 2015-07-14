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
        super(mContext, "onex_db", null, 2);
    }

    public static DatabaseHelper getInstance(Context mContext) {
        if (mInstance == null) {
            mInstance = new DatabaseHelper(mContext);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_feeds(id varchar, type integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_video(id varchar, profile_pic varchar, message varchar, link varchar, likes integer, comment integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_fb_image(id varchar, profile_pic varchar, message varchar, link varchar, likes integer, comment integer)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_remittances (id integer primary key, date varchar, title varchar, message varchar)");
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_financial_tips (id integer, type integer, tips_english varchar, tips_tagalog varchar)");

//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_otop (id integer, province varchar, name varchar, price varchar, product_image varchar, description varchar, ratings int)");
//
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_category (cat_id integer , cat_name varchar)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_subcategory(subcat_id integer , subcat_name varchar)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_products(product_id integer , cat_id integer, subcat_id integer, product_name varchar, product_desc varchar, product_cost integer, product_price integer, product_quantity integer, product_image varchar)");
//        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_grabbed(grabbed_id integer, cat_id integer, subcat_id integer, grabbed_name varchar, grabbed_desc varchar, grabbed_cost integer, grabbed_price integer, grabbed_quantity integer, grabbed_image varchar)");
//
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
        db.execSQL("INSERT INTO tbl_remittances (id, date, title, message) VALUES (1, '01-01-2015', 'SMART', 'This is a sample content') ");
    }

}