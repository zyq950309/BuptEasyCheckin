package bupt.com.bupte;

import android.os.Parcelable;

import java.io.Serializable;

public class Student implements Serializable {//学生类，保存学生信息

    private String name;//姓名
    private int id;//身份证号
    private int sid;//学号
    private int depmt;//院系
    private int prof;//专业
    private int building;//宿舍楼
    private int room;//房间号

    public Student(){//无参构造函数构造空的学生信息
        this.name="空";
        this.id=0;
        this.sid=0;
        this.depmt=0;
        this.prof=0;
        this.building=0;
        this.room=0;
    }

    public Student(String name,int id,int sid,int depmt,int prof,int building,int room){
        this.name=name;
        this.id=id;
        this.sid=sid;
        this.depmt=depmt;
        this.prof=prof;
        this.building=building;
        this.room=room;
    }
    public String getName(){
        return this.name;
    }

    public int getId(){
        return this.id;
    }

    public int getSid(){
        return this.sid;
    }

    public int getDepmt(){
        return this.depmt;
    }

    public int getProf(){
        return this.prof;
    }

    public int getBuilding(){
        return this.building;
    }

    public int getRoom(){
        return this.room;
    }
}
