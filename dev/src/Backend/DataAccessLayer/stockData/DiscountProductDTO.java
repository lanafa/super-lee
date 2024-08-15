package Backend.DataAccessLayer.stockData;

public class DiscountProductDTO {
    public DiscountProductDTO(int discountID, Integer product, String startDate, String endDate,
            double percent) {
        this.discountID = discountID;
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percent = percent;
    }
    private int discountID;
    private Integer product;
    private String startDate;
    private String endDate;
    private double percent;
    public int getDiscountID() {
        return discountID;
    }
    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }
    public Integer getProduct() {
        return product;
    }
    public void setProduct(Integer product) {
        this.product = product;
    }

    public String getStartDate() {
        return startDate.toString();
    }
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    public String getEndDate() {
        return endDate.toString();
    }
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    public double getPercent() {
        return percent;
    }
    public void setPercent(double percent) {
        this.percent = percent;
    }
}
