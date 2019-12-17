package entities;

public class RoomStateTextual implements Entity {
    private Integer id;
    private Integer roomNumber;
    private Integer state;
    private String clientName;
    private CustomDate begin;
    private CustomDate end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public CustomDate getBegin() {
        return begin;
    }

    public void setBegin(CustomDate begin) {
        this.begin = begin;
    }

    public CustomDate getEnd() {
        return end;
    }

    public void setEnd(CustomDate end) {
        this.end = end;
    }
}
