package bupt.com.bupte;

import java.util.ArrayList;
import java.util.List;

public class MyToolClass {

    private static String name="默认姓名";
    private static int distance;
    private static int time;
    private static int distance1;
    private static int time1;
    private static int distance2;
    private static int time2;
    private static int[] num={0,0,0,0,0,0};
    private static List<String> latitude=new ArrayList<String>();//保存纬度
    private static List<String> longitude=new ArrayList<String>();//保存经度

    // 获取当前到指定地点的距离
    public static int getDistance(){
//        distance = (int)(Math.random()*400);
        return distance;
    }
    // 获取当前到指定地点的时间
    public static int getTime(){
//        time = (int)(Math.random()*20);
        return time;
    }

    // 获取当前到指定地点的距离
    public static int getDistance1(){
//        distance = (int)(Math.random()*400);
        return distance1;
    }
    // 获取当前到指定地点的时间
    public static int getTime1(){
//        time = (int)(Math.random()*20);
        return time1;
    }

    // 获取当前到指定地点的距离
    public static int getDistance2(){
//        distance = (int)(Math.random()*400);
        return distance2;
    }
    // 获取当前到指定地点的时间
    public static int getTime2(){
//        time = (int)(Math.random()*20);
        return time2;
    }

    // 获取指定地点的排队人数
    public static String getInLineNumbers(int order){
//        int number = (int)(Math.random()*100);
        int number=num[order-1];
//        int number=num[0];
        String inline = number+""+"人排队";
        return inline;
    }
    //    获取姓名
    public static String getName(){
        return name;
    }

    public static void setLL(List<String> latitude_in,List<String> longitude_in){
        latitude=latitude_in;
        longitude=longitude_in;
    }

    public static List<String> getLatitude(){
        return latitude;
    }

    public static List<String> getLongitude(){
        return longitude;
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

    public static void setDistance1(int distance_in){
        distance1=distance_in;
    }

    public static void setTime1(int time_in){
        time1=time_in;
    }

    public static void setDistance2(int distance_in){
        distance2=distance_in;
    }

    public static void setTime2(int time_in){
        time2=time_in;
    }

    public static void setNum(int[] num_in){
        for(int i=0;i<num_in.length;i++){
            num[i]=num_in[i];
        }
    }
}
