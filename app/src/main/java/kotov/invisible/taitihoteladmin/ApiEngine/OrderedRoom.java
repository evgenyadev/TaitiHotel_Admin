
package kotov.invisible.taitihoteladmin.ApiEngine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderedRoom {

    @SerializedName("child_3_10_count")
    @Expose
    private Integer child310Count;
    @SerializedName("adultsCount")
    @Expose
    private Integer adultsCount;
    @SerializedName("roomsCount")
    @Expose
    private Integer roomsCount;
    @SerializedName("child_3_count")
    @Expose
    private Integer child3Count;
    @SerializedName("roomType")
    @Expose
    private String roomType;

    public Integer getChild310Count() {
        return child310Count;
    }

    public void setChild310Count(Integer child310Count) {
        this.child310Count = child310Count;
    }

    public Integer getAdultsCount() {
        return adultsCount;
    }

    public void setAdultsCount(Integer adultsCount) {
        this.adultsCount = adultsCount;
    }

    public Integer getRoomsCount() {
        return roomsCount;
    }

    public void setRoomsCount(Integer roomsCount) {
        this.roomsCount = roomsCount;
    }

    public Integer getChild3Count() {
        return child3Count;
    }

    public void setChild3Count(Integer child3Count) {
        this.child3Count = child3Count;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

}
