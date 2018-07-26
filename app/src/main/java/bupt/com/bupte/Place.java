package bupt.com.bupte;

/**
 * Created by lenovo on 2018/6/26.
 */

public class Place {
    private int order;
    private String name;
    private String note;
    private String detail;
    private String inLine;


    public Place(int order, String name, String note, String inLine, String detail){
        this.order = order;
        this.name = name;
        this.note = note;
        this.inLine = inLine;
        this.detail = detail;

    }

    public int getOrder() {
        return order;
    }

    public String getName() {
        return name;
    }

    public String getNote() {
        return note;
    }

    public String getDetail() {
        return detail;
    }

    public String getInLine() {
        return inLine;
    }
}
