package Backend.DataAccessLayer.stockData;


public class ItemDTO {
    public ItemDTO(String expDate, int damaged, int expired, int id, int storeamount, int storageamount) {
        this.expDate = expDate;
        this.damaged = damaged;
        this.expired = expired;
        this.id = id;
        this.storeamount = storeamount;
        this.storageamount = storageamount;
    }
    private String expDate;
    private int damaged;
    private int expired;
    private int id;
    private int storeamount;
    private int storageamount;
    public String getExpDate() {
        return expDate.toString();
    }
    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }
    public int getDamaged() {
        return damaged;
    }
    public void setDamaged(int damaged) {
        this.damaged = damaged;
    }
    public int getExpired() {
        return expired;
    }
    public void setExpired(int expired) {
        this.expired = expired;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getStoreamount() {
        return storeamount;
    }
    public void setStoreamount(int storeamount) {
        this.storeamount = storeamount;
    }
    public int getStorageamount() {
        return storageamount;
    }
    public void setStorageamount(int storageamount) {
        this.storageamount = storageamount;
    }
 
}
