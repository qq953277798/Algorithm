package ASearchMethod;

import java.util.Comparator;
import java.util.TreeSet;

public class Item {
        //每一个状态一个id
        int id;
        //当前状态
        String[][] item ;
        //父状态
        int fatherId;
        //动作
        String acter;
        //实际代价
        int price;
        //估计代价
        int estimatePrice;
        //总代价
        int sum ;

    public Item(int id, String[][] item, int fatherId, String acter, int price, int estimatePrice) {
        this.id = id;
        this.item = item;
        this.fatherId = fatherId;
        this.acter = acter;
        this.price = price;
        this.estimatePrice = estimatePrice;
        this.sum=this.price+this.estimatePrice;
    }
    public boolean equals(Item sitem) {
        return (new demo().esPrice(this.item, sitem.item)==0)?true:false;
    }

}
