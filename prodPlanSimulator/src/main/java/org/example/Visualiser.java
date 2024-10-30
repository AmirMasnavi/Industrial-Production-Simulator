package org.example;

import java.util.*;

public class Visualiser {

    //Only to be used in the next Sprint

    private final List<Product> products = new ArrayList<>();
    private final Map<String, List<BillOfMaterials>> bomMap = new HashMap<>();

    public Visualiser() {
        // Initialize products
        products.add(new Product("AS12945T22", "La Belle 22 5l pot", "5l 22 cm aluminium and teflon non stick pot"));
        products.add(new Product("AS12945S22", "Pro 22 5l pot", "5l 22 cm stainless steel pot"));
        products.add(new Product("AS12945S20", "Pro 20 3l pot", "3l 20 cm stainless steel pot"));
        products.add(new Product("AS12945S17", "Pro 17 2l sauce pan", "2l 17 cm stainless steel souce pan"));
        products.add(new Product("AS12945S48", "Pro 17 lid", "17 cm stainless steel lid"));
        products.add(new Product("AS12945G48", "Pro Clear 17 lid", "17 cm glass lid"));

        // Initialize Bill of Materials (BOM)
        bomMap.put("AS12945T22", Arrays.asList(
                new BillOfMaterials("AS12945T22", "PN12344A21", "Screw M6 35 mm", 1),
                new BillOfMaterials("AS12945T22", "PN52384R50", "300x300 mm 5mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945T22", "PN52384R10", "300x300 mm 1mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945T22", "PN18544A21", "Rivet 6 mm", 4),
                new BillOfMaterials("AS12945T22", "PN18544C21", "Stainless steel handle model U6", 2),
                new BillOfMaterials("AS12945T22", "PN18324C54", "Stainless steel handle model R12", 1)
        ));

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

        // Add BOM for other products
        bomMap.put("AS12945S17", Arrays.asList(
                new BillOfMaterials("AS12945S17", "PN52384R10", "300x300 mm 1mm stainless steel sheet", 1),
                new BillOfMaterials("AS12945S17", "PN18544A21", "Rivet 6 mm", 4),
                new BillOfMaterials("AS12945S17", "PN18544C21", "Stainless steel handle model U6", 2)
        ));

        bomMap.put("AS12945S48", Arrays.asList(
                new BillOfMaterials("AS12945S48", "PN18544A21", "Rivet 6 mm", 2),
                new BillOfMaterials("AS12945S48", "PN18544C21", "Stainless steel rim", 1)
        ));

        bomMap.put("AS12945G48", Arrays.asList(
                new BillOfMaterials("AS12945G48", "PN18544C48", "Glass sheet 17 cm", 1),
                new BillOfMaterials("AS12945G48", "PN18544A21", "Rivet 6 mm", 2)
        ));
    }

    // Display all products
    public void listProducts() {
        System.out.println("List of Products:");
        for (int i = 0; i < products.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, products.get(i));
        }
    }

    // Get product selection
    public Product selectProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the number of the product to view its Bill of Materials (BOM):");
        int selection = scanner.nextInt();
        if (selection > 0 && selection <= products.size()) {
            return products.get(selection - 1);
        }
        System.out.println("Invalid selection. Try again.");
        return null;
    }

    // Print the BOM for the selected product in tree format
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

    // Recursive method to print BOM as a tree structure (genealogical tree style)
    private void printBOMTree(List<BillOfMaterials> bomList, int level) {
        String indent = " ".repeat(level * 4);  // Adjust indentation level
        for (BillOfMaterials bom : bomList) {
            System.out.println(indent + "├── " + bom);
            // You can recursively print sub-parts here if needed
        }
    }
}