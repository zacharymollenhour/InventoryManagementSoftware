package Models;

public class InHouse extends Part{
    /**
     * machineId : int
     */
    private int machineId;

    /**
     * InHouse(id : int, name : String,
     * price : double, stock : int, min : int, max : int, machineId:int)
     * @param ID
     * @param Name
     * @param Price
     * @param Stock
     * @param Min
     * @param Max
     * @param machineId
     */
    public InHouse(int ID, String Name, double Price, int Stock, int Min, int Max, int machineId)
    {
        super(ID,Name,Price,Stock,Min,Max);
        this.machineId = machineId;
    }

    /**
     * setMachineId(machineId:int):void
     * @param machineId
     */
    public void setMachineID(int machineId)
    {
        this.machineId = machineId;
    }

    /**
     * getMachineId():int
     * @return
     */
    public int getMachineId()
    {
        return machineId;
    }
}
