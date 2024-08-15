package Backend.DataAccessLayer.stockData;

public class DiscountCategoryDTO {
    public DiscountCategoryDTO(int discountID, String category, String startDate, String endDate,
            double percent) {
        this.discountID = discountID;
        this.category = category;
        this.startDate = startDate;
        this.endDate = endDate;
        this.percent = percent;
    }
    private int discountID;
    private String category;
    private String startDate;
    private String endDate;
    private double percent;
    public int getDiscountID() {
        return discountID;
    }
    public void setDiscountID(int discountID) {
        this.discountID = discountID;
    }
    public String getCategory() {
        return category;
    }
    public void setProduct(String category) {
        this.category = category;
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
