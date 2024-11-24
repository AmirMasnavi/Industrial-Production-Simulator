package org.example;

import java.util.*;

/**
 * The `Visualiser` class provides a mechanism to visualize products, their Bills of Materials (BOM),
 * and Bills of Operations (BOO). It simulates the structure and operations necessary to display
 * manufacturing details of products in a hierarchical (tree-like) format.
 */
public class Visualiser {

    // List that stores all products
    final List<Product> products = new ArrayList<>();
    // Map that stores the Bill of Materials (BOM) for each product, identified by product code
    private final Map<String, List<BillOfMaterials>> bomMap = new HashMap<>();
    // Map that stores the Bill of Operations (BOO) for each product, identified by product code
    private final Map<String, List<BillOfOperations>> booMap = new HashMap<>();

    /**
     * The constructor initializes products, BOM, and BOO data.
     */
    public Visualiser() {
        // Initialize the product list
        products.add(new Product("AS12945T22", "La Belle 22 5l pot", "5l 22 cm aluminium and teflon non stick pot"));
        products.add(new Product("AS12945S22", "Pro 22 5l pot", "5l 22 cm stainless steel pot"));
        products.add(new Product("AS12945S20", "Pro 20 3l pot", "3l 20 cm stainless steel pot"));
        products.add(new Product("AS12945S17", "Pro 17 2l pot", "2l 17 cm stainless steel pot"));
        products.add(new Product("AS12945P17", "Pro 17 2l sauce pan", "2l 17 cm stainless steel sauce pan"));
        products.add(new Product("AS12945S48", "Pro 17 lid", "17 cm stainless steel lid"));
        products.add(new Product("AS12945G48", "Pro Clear 17 lid", "17 cm glass lid"));

        // Initialize the BOM for each product, associating product codes with components
        bomMap.put("AS12945S22", Arrays.asList(
                new BillOfMaterials("AS12945S22", "PN12344A21", "Screw M6 35 mm", 1),
                new BillOfMaterials("AS12945S22", "PN52384R50", "300x300 mm 5mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S22", "PN52384R10", "300x300 mm 1mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S22", "PN18544A21", "Rivet 6 mm", 4),
                new BillOfMaterials("AS12945S22", "PN18544C21", "Stainless steel handle model U6", 2),
                new BillOfMaterials("AS12945S22", "PN18324C54", "Stainless steel handle model R12", 1)
        ));
        bomMap.put("AS12945S20", Arrays.asList(
                new BillOfMaterials("AS12945S20", "PN12344A21", "Screw M6 35 mm", 1),
                new BillOfMaterials("AS12945S20", "PN52384R50", "300x300 mm 5mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S20", "PN52384R10", "300x300 mm 1mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S20", "PN18544A21", "Rivet 6 mm", 4),
                new BillOfMaterials("AS12945S20", "PN18544C21", "Stainless steel handle model U6", 2),
                new BillOfMaterials("AS12945S20", "PN18324C51", "Stainless steel handle model R11", 1)
        ));
        bomMap.put("AS12945S17", Arrays.asList(
                new BillOfMaterials("AS12945S17", "PN12344A21", "Screw M6 35 mm", 1),
                new BillOfMaterials("AS12945S17", "PN52384R45", "250x250 mm 5mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S17", "PN52384R12", "250x250 mm 1mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S17", "PN18544A21", "Rivet 6 mm", 4),
                new BillOfMaterials("AS12945S17", "PN18544C21", "Stainless steel handle model U6", 2),
                new BillOfMaterials("AS12945S17", "PN18324C51", "Stainless steel handle model R11", 1)
        ));
        bomMap.put("AS12945P17", Arrays.asList(
                new BillOfMaterials("AS12945P17", "PN52384R45", "250x250 mm 5mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945P17", "PN18324C91", "Stainless steel handle model S26", 1)
        ));
        bomMap.put("AS12945S48", Arrays.asList(
                new BillOfMaterials("AS12945S48", "PN18544A21", "Rivet 6 mm", 2),
                new BillOfMaterials("AS12945S48", "PN18544C21", "Stainless steel rim", 1)
        ));
        bomMap.put("AS12945G48", Arrays.asList(
                new BillOfMaterials("AS12945G48", "PN18544C48", "Glass sheet 17 cm", 1),
                new BillOfMaterials("AS12945G48", "PN18544A21", "Rivet 6 mm", 2)
        ));

        // Initialize the BOO for each product, associating product codes with necessary operations
        booMap.put("AS12945S22", Arrays.asList(
                new BillOfOperations("AS12945S22", "5647", "Disc cutting", 1),
                new BillOfOperations("AS12945S22", "5647", "Disc cutting", 2),
                new BillOfOperations("AS12945S22", "5649", "Initial pot base pressing", 3),
                new BillOfOperations("AS12945S22", "5651", "Final pot base pressing", 4),
                new BillOfOperations("AS12945S22", "5653", "Pot base finishing", 5),
                new BillOfOperations("AS12945S22", "5659", "Pot handles riveting", 6),
                new BillOfOperations("AS12945S22", "5669", "Pot base polishing", 7),
                new BillOfOperations("AS12945S22", "5655", "Lid pressing", 8),
                new BillOfOperations("AS12945S22", "5657", "Lid finishing", 9),
                new BillOfOperations("AS12945S22", "5661", "Lid handle screw", 10),
                new BillOfOperations("AS12945S22", "5667", "Lid polishing", 11),
                new BillOfOperations("AS12945S22", "5663", "Pot test and packaging", 12)
        ));

        booMap.put("AS12945S20", Arrays.asList(
                new BillOfOperations("AS12945S20", "5647", "Disc cutting", 1),
                new BillOfOperations("AS12945S20", "5647", "Disc cutting", 2),
                new BillOfOperations("AS12945S20", "5649", "Initial pot base pressing", 3),
                new BillOfOperations("AS12945S20", "5651", "Final pot base pressing", 4),
                new BillOfOperations("AS12945S20", "5653", "Pot base finishing", 5),
                new BillOfOperations("AS12945S20", "5659", "Pot handles riveting", 6),
                new BillOfOperations("AS12945S20", "5669", "Pot base polishing", 7),
                new BillOfOperations("AS12945S20", "5655", "Lid pressing", 8),
                new BillOfOperations("AS12945S20", "5657", "Lid finishing", 9),
                new BillOfOperations("AS12945S20", "5661", "Lid handle screw", 10),
                new BillOfOperations("AS12945S20", "5667", "Lid polishing", 11),
                new BillOfOperations("AS12945S20", "5663", "Pot test and packaging", 12)
        ));

        booMap.put("AS12945S17", Arrays.asList(
                new BillOfOperations("AS12945S17", "5647", "Disc cutting", 1),
                new BillOfOperations("AS12945S17", "5647", "Disc cutting", 2),
                new BillOfOperations("AS12945S17", "5649", "Initial pot base pressing", 3),
                new BillOfOperations("AS12945S17", "5651", "Final pot base pressing", 4),
                new BillOfOperations("AS12945S17", "5653", "Pot base finishing", 5),
                new BillOfOperations("AS12945S17", "5659", "Pot handles riveting", 6),
                new BillOfOperations("AS12945S17", "5669", "Pot base polishing", 7),
                new BillOfOperations("AS12945S17", "5655", "Lid pressing", 8),
                new BillOfOperations("AS12945S17", "5657", "Lid finishing", 9),
                new BillOfOperations("AS12945S17", "5661", "Lid handle screw", 10),
                new BillOfOperations("AS12945S17", "5667", "Lid polishing", 11),
                new BillOfOperations("AS12945S17", "5663", "Pot test and packaging", 12)
        ));

        booMap.put("AS12945P17", Arrays.asList(
                new BillOfOperations("AS12945P17", "5647", "Sauce pan base pressing", 1),
                new BillOfOperations("AS12945P17", "5649", "Sauce pan finishing", 2),
                new BillOfOperations("AS12945P17", "5655", "Sauce pan handle riveting", 3),
                new BillOfOperations("AS12945P17", "5663", "Sauce pan test and packaging", 4)
        ));

        booMap.put("AS12945S48", Arrays.asList(
                new BillOfOperations("AS12945S48", "5647", "Lid disc cutting", 1),
                new BillOfOperations("AS12945S48", "5653", "Lid finishing", 2),
                new BillOfOperations("AS12945S48", "5655", "Lid riveting", 3),
                new BillOfOperations("AS12945S48", "5667", "Lid polishing", 4)
        ));

        booMap.put("AS12945G48", Arrays.asList(
                new BillOfOperations("AS12945G48", "5647", "Glass lid cutting", 1),
                new BillOfOperations("AS12945G48", "5653", "Glass lid finishing", 2),
                new BillOfOperations("AS12945G48", "5655", "Glass lid riveting", 3),
                new BillOfOperations("AS12945G48", "5667", "Glass lid polishing", 4)
        ));
    }

    /**
     * Displays all the available products in the system.
     */
    public void listProducts() {
        System.out.println("Product List:");
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, products.get(i));
        }
    }

    /**
     * Allows the user to select a product from the list to view its BOM or BOO.
     *
     * @return The selected product, or null if the selection is invalid.
     */
    public Product selectProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the product number to view its Bill of Materials (BOM):");
        int selection = scanner.nextInt();
        if (selection > 0 && selection <= products.size()) {
            return products.get(selection - 1);
        }
        System.out.println("Invalid selection. Please try again.");
        return null;
    }

    /**
     * Prints the Bill of Materials (BOM) for the selected product in a tree format.
     *
     * @param product The product whose BOM will be displayed.
     */
    public void printBOM(Product product) {
        if (product == null) return;
        List<BillOfMaterials> bomList = bomMap.get(product.getCode());
        if (bomList != null) {
            System.out.println("\nBill of Materials Tree for " + product.getName() + ":");
            printBOMTree(bomList, 0);
        } else {
            System.out.println("No BOM found for this product.");
        }
    }

    /**
     * Recursive method to print the BOM structure in tree format.
     *
     * @param bomList The list of BOM items to be printed.
     * @param level   The current indentation level.
     */
    private void printBOMTree(List<BillOfMaterials> bomList, int level) {
        String indent = " ".repeat(level * 4);  // Create indentation based on the level
        for (BillOfMaterials bom : bomList) {
            System.out.println(indent + "├── " + bom);
        }
    }

    /**
     * Prints the Bill of Operations (BOO) for the selected product in a tree format.
     *
     * @param product The product whose BOO will be displayed.
     */
    public void printBOO(Product product) {
        if (product == null) return;
        List<BillOfOperations> booList = booMap.get(product.getCode());
        if (booList != null) {
            System.out.println("\nBill of Operations Tree for " + product.getName() + ":");
            printBOOTree(booList, 0);
        } else {
            System.out.println("No BOO found for this product.");
        }
    }

    /**
     * Recursive method to print the BOO structure in tree format.
     *
     * @param booList The list of BOO items to be printed.
     * @param level   The current indentation level.
     */
    private void printBOOTree(List<BillOfOperations> booList, int level) {
        String indent = " ".repeat(level * 4);  // Create indentation based on the level
        for (BillOfOperations boo : booList) {
            System.out.println(indent + "├── " + boo);
        }
    }
}
