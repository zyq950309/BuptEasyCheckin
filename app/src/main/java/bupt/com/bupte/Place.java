package bupt.com.bupte;

/**
 * Created by lenovo on 2018/6/26.
 */

public class Place {
    private int order;
    private String name;
    private String detail;
    private int style_tag;
//    private String inLine;


    public Place(int order, String name, String detail){
        this.order = order;
        this.name = name;
//        this.inLine = inLine;
        this.detail = detail;

    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }

//    public String getInLine() {
//        return inLine;
//    }
}
