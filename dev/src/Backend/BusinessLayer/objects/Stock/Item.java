package Backend.BusinessLayer.objects.Stock;
import java.time.LocalDate;

import Backend.DataAccessLayer.stockData.ItemDTO;

// this is an item that is already in the store
public class Item {

    private LocalDate expDate;
    private int damaged;
    private int expired;
    private int id;
    private int storeamount;
    private int storageamount;
    private ItemDTO itemDTO;


    public ItemDTO getItemDTO() {
        return itemDTO;
    }
    public void setItemDTO(ItemDTO itemDTO) {
        this.itemDTO = itemDTO;
    }
    public int getStoreamount() {
        return storeamount;
    }
    public int getDamaged() {
        return damaged;
    }
    public int getStorageamount() {
        return storageamount;
    }
    public void addStoreItem(int amount) {
        this.storeamount += amount;
    }
    public void addStorageItem(int amount) {
        this.storageamount+=amount;
    }
    public int getExpired() {
        return expired;
    }


    public int getfullAmount() {
        return storageamount+storeamount;
    }

    public Item(int id, LocalDate expDate,int storageamount,int storeamount) {
        // if (expDate.isBefore(LocalDate.now())) {
        //     throw new IllegalArgumentException("cannot add item The given date is in the past.");
        // }
        this.storageamount=storageamount;
        this.storeamount=storeamount;
        this.expDate = expDate;
        damaged = 0;
        expired=0;
        this.itemDTO=new ItemDTO(expDate.toString(), storeamount, storeamount, id, storeamount, storageamount);
        this.id=id;

    }

    public void isExpired() {
            expired+=storageamount+storeamount;
            storageamount=0;
            storeamount=0;
    }



    public int getId() {
        return id;
    }





    public LocalDate getExpDate() {
        return expDate;
    }

    public void setExpDate(LocalDate expDate) {
        this.expDate = expDate;
    }

    public void addDmgstore(int amount) {
        damaged+=amount;
        storeamount-=amount;

    }
    public void addDmgstorage(int amount) {
        damaged+=amount;
        storageamount-=amount;

    }
    public void removestorageitem(int amount) {
        storageamount-=amount;

    }  public void removeStoreItem(int amount) {
        storeamount-=amount;

    }
    public void setDamaged(int damaged2) {
        damaged=damaged2;
    }



}