package entities;

public class ProvidedService implements Entity {
    private Integer id;
    private Integer clientId;
    private Integer hotelEmployeeId;
    private Integer serviceId;
    private CustomDate date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getClientId() {
        return clientId;
    }

    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    public Integer getHotelEmployeeId() {
        return hotelEmployeeId;
    }

    public void setHotelEmployeeId(Integer hotelEmployeeId) {
        this.hotelEmployeeId = hotelEmployeeId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date = date;
    }
}
