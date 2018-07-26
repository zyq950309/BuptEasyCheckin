package bupt.com.bupte;

public class MyToolClass {
    // order指定地点
    // 获取当前到指定地点的距离
    public static int getDistance(int order){
        int distance = (int)(Math.random()*400);
        return distance;
    }
    // 获取当前到指定地点的时间
    public static int getTime(int order){
        int time = (int)(Math.random()*20);
        return time;
    }
    // 获取指定地点的排队人数
    public static String getInLineNumbers(int order){
        int number = (int)(Math.random()*100);
        String inline = number+""+"人排队";
        return inline;
    }
//    获取姓名
    public static String getName(){
        return "张三";
    }
}
