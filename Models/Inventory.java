package Models;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    /**
     * allParts:ObservableList<Part>
     * allProducts:ObservableList<Product>
     */
    private static final ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static final ObservableList<Product> allProducts = FXCollections.observableArrayList();


    /**
     * Function to help keep track of the next partID value
     * Used when Adding a new part
     */
    public static int getNextPartID()
    {
        int maxID = 0;
        for(Part part : allParts)
        {
            int partID = part.getId();
            if (partID > maxID)
            {
                maxID = partID;
            }
        }
        return maxID;
    }

    /**
     * addPart(newPart:Part):void
     * @param newPart
     */
    public static void addPart(Part newPart)
    {
        allParts.add(newPart);
    }

    /**
     * addProduct(newProduct:Product):void
     * @param newProduct
     */
    public static void addProduct(Product newProduct)
    {
        allProducts.add(newProduct);
    }

    /**
     * lookupPart(partId:int):Part
     * @param partID
     * @return
     */
    public static Part lookupPart(int partID)
    {
        //loop through allParts
        for (Part part : allParts)
        {
            //search for the given partID parameter
            if(part.getId() == partID)
            {
                //If found, return the part
                return part;
            }
        }
        //If above is not triggered we will return a null value
        //Since no part exists
        return null;
    }

    /**
     * lookupProduct(productId:int):Product
     * @param productID
     * @return
     */
    public static Product lookupProduct(int productID)
    {
        //loop through allProducts
        for (Product product : allProducts)
        {
            //search for the given productID parameter
            if(product.getID() == productID)
            {
                //If found, return the product
                return product;
            }
        }
        //If above is not triggered we will return a null value
        //Since no product exists
        return null;
    }

    /**
     * lookupPart(partName:String):ObservableList<Part>
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName)
    {
        ObservableList<Part> lookupResults = FXCollections.observableArrayList();
        partName = partName.toLowerCase();

        //Loop through allParts ObservableArrayList
        for (Part part : allParts)
        {
            //If the current part iteration is equal to the given partName
            if(part.getName().toLowerCase().contains(partName))
            {
                //Add it to the list of lookupResults
                lookupResults.add(part);
            }
        }
        return lookupResults;
    }

    /**
     * lookupProduct(productName:String):ObservableList<Product>
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName)
    {
        ObservableList<Product> lookupResults = FXCollections.observableArrayList();
        productName = productName.toLowerCase();

        //Loop through allProducts ObservableArrayList
        for (Product product : allProducts)
        {
            //If the current Product iteration is equal to the given Productname
            if(product.getName().toLowerCase().contains(productName))
            {
                //Add it to the list of lookupResults
                lookupResults.add(product);
            }
        }
        return lookupResults;
    }

    /**
     * updatePart(index:int, selectedPart:Part):void
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart)
    {
        allParts.set(index, selectedPart);
    }

    /**
     * updateProduct(index:int, newProduct:Product):void
     */
    public static void updateProduct(int index, Product newProduct)
    {
        allProducts.set(index, newProduct);
    }

    /**
     * deletePart(selectedPart:Part):boolean
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart)
    {
        return allParts.remove(selectedPart);
    }

    /**
     * deleteProduct(selectedProduct:Product):boolean
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct)
    {
        return allProducts.remove(selectedProduct);
    }

    /**
     * getAllParts():ObservableList<Part>
     * @return
     */
    public static ObservableList<Part> getAllParts()
    {
        return allParts;
    }

    /**
     * getAllProducts():ObservableList<Product>
     * @return
     */
    public static ObservableList<Product> getAllProducts()
    {
        return allProducts;
    }
}
