package util;

/**
 * Created by maheshsagar on 09/05/15.
 */
public interface BillConstants {

    enum Element_Type{BILL,ITEM,
        TEXT, IMAGE,DATE,LOCATION,PHONE_NUMBER,EMAIL,PRICE,WARRANTY,PURCHASE_DATE,BILL_DUE_DATE,HASH_TAG
    }
    String[] elementKey = {"BILL","ITEM",
            "TEXT", "IMAGE","DATE","LOCATION","PHONE_NUMBER","EMAIL","PRICE","WARRANTY","PURCHASE_DATE"
            ,"BILL_DUE_DATE","HASH_TAG"};
    enum Category_Type{BILL_ONE_TIME,BILL_UTILITY,ITEM,WARRANTY,PURCHASE_DATE,BILL_DUE_DATE
    }
    String billName = "bill_name";
    String bill_image = "bill_image";
    String item_image = "item_image";
    String hash_tag = "hash_tag";
}
