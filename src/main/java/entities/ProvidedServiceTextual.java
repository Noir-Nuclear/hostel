package entities;


public class ProvidedServiceTextual implements Entity {
    private Integer id;
    private String clientName;
    private String hotelEmployeeName;
    private String serviceName;
    private CustomDate date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getHotelEmployeeName() {
        return hotelEmployeeName;
    }

    public void setHotelEmployeeName(String hotelEmployeeName) {
        this.hotelEmployeeName = hotelEmployeeName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date = date;
    }
}
