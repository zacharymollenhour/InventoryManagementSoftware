package Models;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
/**
 * Product Class
 * @author Zachary mollenhour
 */
public class Product {
    //Initialize FXML Components
    private int ID;
    private String Name;
    private double Price;
    private int Stock;
    private int Min;
    private int Max;
    private final ObservableList<Part> AssociatedParts;

    /**
     * Create a default constructor for the Product Class
     */
    public Product(int ID, String Name, double Price, int Stock, int Min, int Max){
        this.ID = ID;
        this.Name = Name;
        this.Price = Price;
        this.Stock = Stock;
        this.Min = Min;
        this.Max = Max;

        //Initialize the Observable List
        this.AssociatedParts = FXCollections.observableArrayList();
    }

    //Helper Functions for the Product Class
    //+ Product(id : int, name : String,
    //  price : double, stock : int, min : int, max : int)
    //+ setId(id:int):void
    //+ setName(name:String):void
    //+ setPrice(price:double):void
    //+ setStock(stock:int):void
    //+ setMin(min:int):void
    //+ setMax(max:int):void
    //+ getId():int
    //+ getName():String
    //+ getPrice():double
    //+ getStock():int
    //+ getMin():int
    //+ getMax():int
    //+ addAssociatedPart(part:Part):void
    //+ deleteAssociatedPart(selectedAssociatedPart:Part):boolean
    //+ getAllAssociatedParts():ObservableList<Part>
    /**
     * setId(id:int):void
     * @param ID
     */
    public void setID(int ID){
        this.ID = ID;
    }

    /**
     * setName(name:String):void
     * @param Name
     */
    public void setName(String Name)
    {
        this.Name = Name;
    }

    /**
     * setPrice(price:double):void
     * @param Price
     */
    public void setPrice(double Price){
        this.Price = Price;
    }

    /**
     * setStock(stock:int):void
     * @param Stock
     */
    public void setStock(int Stock)
    {
        this.Stock = Stock;
    }

    /**
     * setMin(min:int):void
     * @param Min
     */
    public void setMin(int Min)
    {
        this.Min = Min;
    }

    /**
     * setMax(max:int):void
     * @param Max
     */
    public void setMax(int Max)
    {
        this.Max = Max;
    }

    //Get Helper Functions

    /**
     * getId():int
     * @return
     */
    public int getID()
    {
        return ID;
    }

    /**
     * getName():String
     * @return
     */
    public String getName(){
        return Name;
    }

    /**
     * getPrice():double
     * @return
     */
    public double getPrice()
    {
        return Price;
    }

    /**
     * getStock():int
     * @return
     */
    public int getStock()
    {
        return Stock;
    }

    /**
     * getMin():int
     * @return
     */
    public int getMin()
    {
        return Min;
    }

    /**
     * getMax():int
     * @return
     */
    public int getMax()
    {
        return Max;
    }

    /**
     * addAssociatedPart(part:Part):void
     * @param part
     */
    public void addAssociatedPart(Part part)
    {
        AssociatedParts.add(part);
    }

    /**
     * deleteAssociatedPart(selectedAssociatedPart:Part):boolean
     * @param selectedAssociatedPart
     * @return
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart)
    {
        return AssociatedParts.remove(selectedAssociatedPart);
    }

    /**
     * getAllAssociatedParts():ObservableList<Part>
     * @return
     */
    public ObservableList<Part> getAllAssociatedParts()
    {
        return AssociatedParts;
    }
}
