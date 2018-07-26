package bupt.com.bupte;

import java.util.List;

/**
 * Created by kai on 2018/7/16.
 */

public class Site_ll {

    /**
     * code : 0
     * latitude : ["39.96629391677764","39.97070391677764","39.964516","39.958734","39.967113916777635"]
     * longitude : ["116.36299162025452","116.36379162025452","116.367564","116.362807","116.36479162025452"]
     */

    private String code;
    private List<String> latitude;
    private List<String> longitude;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<String> getLatitude() {
        return latitude;
    }

    public void setLatitude(List<String> latitude) {
        this.latitude = latitude;
    }

    public List<String> getLongitude() {
        return longitude;
    }

    public void setLongitude(List<String> longitude) {
        this.longitude = longitude;
    }
}
