package bupt.com.bupte;

import java.util.List;

public class MyToolClass {

    private static String name="默认姓名";
    private static int distance;
    private static int time;
    private static int[] num={0,0,0,0,0};
    // order指定地点
    // 获取当前到指定地点的距离
    public static int getDistance(int order){
//        distance = (int)(Math.random()*400);
        return distance;
    }
    // 获取当前到指定地点的时间
    public static int getTime(int order){
//        time = (int)(Math.random()*20);
        return time;
    }
    // 获取指定地点的排队人数
    public static String getInLineNumbers(int order){
//        int number = (int)(Math.random()*100);
//        int number=num[order-1];
        int number=num[0];
        String inline = number+""+"人排队";
        return inline;
    }
    //    获取姓名
    public static String getName(){
        return name;
    }

    public static void setName(String name_in){
        name=name_in;
    }

    public static void setDistance(int distance_in){
        distance=distance_in;
    }

    public static void setTime(int time_in){
        time=time_in;
    }

    public static void setNum(int[] num_in){
        for(int i=0;i<num_in.length;i++){
            num[i]=num_in[i];
        }
    }
}
