package Models;

public class Outsourced extends Part {
    /**
     * companyName : String
     */
    private String companyName;

    /**
     * Outsourced(id : int, name : String,
     * price : double, stock : int, min : int, max : int,
     * companyName:String)
     * @param id
     * @param name
     * @param price
     * @param stock
     * @param min
     * @param max
     * @param companyName
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName)
    {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }

    /**
     * setCompanyName(companyName:String):void
     * @param companyName
     */
    public void setCompanyName(String companyName)
    {
        this.companyName = companyName;
    }

    /**
     * getCompanyName():String
     * @return
     */
    public String getCompanyName()
    {
        return companyName;
    }


}
