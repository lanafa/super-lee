package Backend.DataAccessLayer.stockData;

public class CategoryDTO {


    private String name;
    private String faString;

    public String getFaString() {
        return faString;
    }

    public void setFaString(String faString) {
        this.faString = faString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CategoryDTO(String name,String fString) {
        this.name = name;
        this.faString=fString;
        
    }
}
