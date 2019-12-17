package entities;

public class RoomState implements Entity {
    private Integer id;
    private Integer roomId;
    private Integer state;
    private Integer clientId;
    private CustomDate begin;
    private CustomDate end;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRoomId() {
        return roomId;
    }

    public void setRoomId(Integer roomId) {
        this.roomId = roomId;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
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
