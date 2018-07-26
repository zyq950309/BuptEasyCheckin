package bupt.com.bupte;

public class Student_info {
    /**
     * code : 0
     * IsLoginOk : 1
     * info : {"name":"张一","id":"123456","sid":"2018000001","depmt":"2","prof":"1","building":"5","room":"304"}
     */

    private int code;
    private int IsLoginOk;
    private InfoBean info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getIsLoginOk() {
        return IsLoginOk;
    }

    public void setIsLoginOk(int IsLoginOk) {
        this.IsLoginOk = IsLoginOk;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * name : 张一
         * id : 123456
         * sid : 2018000001
         * depmt : 2
         * prof : 1
         * building : 5
         * room : 304
         */

        private String name;
        private String id;
        private String sid;
        private String depmt;
        private String prof;
        private String building;
        private String room;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public String getDepmt() {
            return depmt;
        }

        public void setDepmt(String depmt) {
            this.depmt = depmt;
        }

        public String getProf() {
            return prof;
        }

        public void setProf(String prof) {
            this.prof = prof;
        }

        public String getBuilding() {
            return building;
        }

        public void setBuilding(String building) {
            this.building = building;
        }

        public String getRoom() {
            return room;
        }

        public void setRoom(String room) {
            this.room = room;
        }
    }
}
