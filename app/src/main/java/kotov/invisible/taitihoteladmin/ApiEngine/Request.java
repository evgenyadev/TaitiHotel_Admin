package kotov.invisible.taitihoteladmin.ApiEngine;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Request {

    @SerializedName("time_to")
    @Expose
    private Integer timeTo;
    @SerializedName("orderedRoomData")
    @Expose
    private List<OrderedRoom> orderedRoomsList = new ArrayList<OrderedRoom>();
    @SerializedName("phone")
    @Expose
    private String phone;
    @SerializedName("time_from")
    @Expose
    private Integer timeFrom;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("date_check_in")
    @Expose
    private String date_check_in;
    @SerializedName("date_check_out")
    @Expose
    private String date_check_out;

    public String getDate_check_in() {
        return date_check_in;
    }

    public void setDate_check_in(String date_check_in) {
        this.date_check_in = date_check_in;
    }

    public String getDate_check_out() {
        return date_check_out;
    }

    public void setDate_check_out(String date_check_out) {
        this.date_check_out = date_check_out;
    }

    public Integer getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(Integer timeTo) {
        this.timeTo = timeTo;
    }

    public List<OrderedRoom> getOrderedRoomsList() {
        return orderedRoomsList;
    }

    public void setOrderedRoomsList(List<OrderedRoom> orderedRoomsList) {
        this.orderedRoomsList = orderedRoomsList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(Integer timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        StringBuilder description = new StringBuilder();

        description.append("Звонить с ");
        description.append(timeFrom);
        description.append(":00 до ");
        description.append(timeTo);
        description.append(":00");

        description.append("\nДата заезда: ");
        description.append(date_check_in);
        description.append("\nДата выезда: ");
        description.append(date_check_out);
        description.append("\n\nЗаказ:");

        for (OrderedRoom roomData : orderedRoomsList) {
            description.append("\nТип: \"");
            description.append(roomData.getRoomType());
            description.append("\"\nКол-во комнат: ");
            description.append(roomData.getRoomsCount());

            if (roomData.getAdultsCount() != 0) {
                description.append("\nКол-во взрослых: ");
                description.append(roomData.getAdultsCount());
            }
            if (roomData.getChild310Count() != 0 || roomData.getChild3Count() != 0)
                description.append("\nКол-во детей:");

            if (roomData.getChild310Count() != 0) {
                description.append("\nот 3-х до 10-ти лет: ");
                description.append(roomData.getChild310Count());
            }
            if (roomData.getChild3Count() != 0) {
                description.append("\nдо 3 лет: ");
                description.append(roomData.getChild3Count());
            }
            description.append("\n");
        }

        return description.toString();
    }
}
