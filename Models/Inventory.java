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
    private static String lastPartSearch = "";
    private static String lastProductSearch = "";
    private static ObservableList<Part> filteredParts = FXCollections.observableArrayList();
    private static ObservableList<Product> filteredProducts = FXCollections.observableArrayList();
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
        return maxID + 1;
    }
    public static int getNextProductID() {
        int maxID = 0;
        for (Product product : allProducts) {
            int id = product.getID();
            if (id > maxID) {
                maxID = id;
            }
        }
        return maxID + 1;
    }
    public static ObservableList<Product> getFilteredProducts(String searchQuery) {
        return getFilteredProducts(searchQuery, false);
    }

    /**
     * Filters the Products Inventory by a user-provided string.
     * Part names and IDs are matched in a case-insensitive fashion and are stored in a static variable.
     * @param searchQuery
     * @param reset
     */
    public static ObservableList<Product> getFilteredProducts(String searchQuery, boolean reset) {

        if (!reset && searchQuery.equals(lastProductSearch)) {
            return filteredProducts;
        }

        filteredProducts.clear();
        // If the search query is a positive integer, we want to lookup a Product whose ID matches.
        // However, it's possible that the product name also has digits, so we want to return those as well.
        if (searchQuery.matches("\\d+")) {
            int lookupId = Integer.parseInt(searchQuery);
            Product foundProduct = Inventory.lookupProduct(lookupId);
            if (foundProduct != null) {
                filteredProducts.add(foundProduct);
            }
            ObservableList<Product> foundProducts = Inventory.lookupProduct(searchQuery);

            foundProducts.removeIf(p -> (p.getID() == lookupId));
            filteredProducts.addAll(foundProducts);
        } else {
            filteredProducts = Inventory.lookupProduct(searchQuery);
        }


        lastProductSearch = searchQuery;
        return filteredProducts;
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
     * Filters the Parts Inventory by a user-provided string.
     * Part names and IDs are matched in a case-insensitive fashion and are stored in a static variable.
     * @param searchQuery
     */
    public static ObservableList<Part> getFilteredParts(String searchQuery) {
        return getFilteredPartList(searchQuery, false);
    }

    /**
     * Filters the Parts Inventory by a user-provided string.
     * Part names and IDs are matched in a case-insensitive fashion and are stored in a static variable.
     * @param reset
     * @param searchQuery
     */
    public static ObservableList<Part> getFilteredPartList(String searchQuery, boolean reset) {
        // If we get a query for the same string twice in a row, no extra work needs to be done.
        if (!reset && searchQuery.equals(lastPartSearch)) {
            return filteredParts;
        }

        filteredParts.clear();


        if (searchQuery.matches("\\d+")) {
            int lookupId = Integer.parseInt(searchQuery);
            Part foundPart = Inventory.lookupPart(lookupId);
            if (foundPart != null) {
                filteredParts.add(foundPart);
            }
            ObservableList<Part> foundParts = Inventory.lookupPart(searchQuery);
            // There's a chance that lookupPart will include the Part found earlier, so ensure it's not duplicated.
            foundParts.removeIf(p -> (p.getId() == lookupId));
            filteredParts.addAll(foundParts);
        } else {
            filteredParts = Inventory.lookupPart(searchQuery);
        }

        // Store the search string associated with the current state of filteredParts
        lastPartSearch = searchQuery;
        return filteredParts;
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
