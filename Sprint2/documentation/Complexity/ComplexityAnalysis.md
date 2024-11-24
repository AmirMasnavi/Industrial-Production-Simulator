# Complexity

## Complexity Analysis:

### USEI08
> **buildTree**

`````java
    public ProductionTreeNode buildTree(int itemId, Map<Integer, Integer> operationToItemMap) {
        Item rootItem = findItemById(itemId);
        ProductionTreeNode rootNode = new ProductionTreeNode(rootItem);

        // Set root node quantity using itemQuantities
        if (itemQuantities.containsKey(itemId)) {
        rootNode.setQuantity(itemQuantities.get(itemId));
        }

        Set<Integer> visitedItems = new HashSet<>();
        Set<Integer> addedOperations = new HashSet<>();
        buildSubTree(rootNode, visitedItems, addedOperations, operationToItemMap);

        return rootNode;
        }
``````

***Complexity Analysis:***

**Finding the root item:**
- `findItemById(itemId)` searches for an item by ID.
- Complexity depends on cthe data structure:
- If a linear search is used, O(n), where n is the number of items.
- If items are stored in a HashMap, O(1).

**Creating the root node:**
- Instantiating `ProductionTreeNode` and setting the quantity are O(1).

**Building the subtree:**
- Calls `buildSubTree` to recursively construct the tree.
- Each item and operation is processed once using `visitedItems` and `addedOperations` (O(1) checks/additions).
- Total complexity of `buildSubTree`: O(i + p + e), where:
- i = number of items.
- p = number of operations.
- e = number of edges in `booData` and `operationToItemMap`.

**Total Complexity:**
- Let:
- n = number of items in `findItemById`.
- i, p, e as described above.
- Overall complexity: **O(n + i + p + e)**.


> **buildSubTree**

`````java
    private void buildSubTree(ProductionTreeNode node, Set<Integer> visitedItems, Set<Integer> addedOperations, Map<Integer, Integer> operationToItemMap) {
        Item item = node.getItem();

        // Prevent infinite recursion by skipping already visited items
        if (visitedItems.contains(item.getId())) {
        return;
        }

        visitedItems.add(item.getId());

        // Check if this item has any subcomponents
        if (booData.containsKey(item.getId())) {
        Map<Integer, Double> subcomponents = booData.get(item.getId());

        // Add operation node if it exists for this item
        Operation operation = findOperationByItemId(item.getId(), operationToItemMap);
        if (operation != null && !addedOperations.contains(operation.getId())) {
        ProductionTreeNode operationNode = new ProductionTreeNode(operation);
        node.addChild(operationNode); // Add operation as a direct child of the item
        addedOperations.add(operation.getId());
        }

        // Process subcomponents
        for (Map.Entry<Integer, Double> subcomponent : subcomponents.entrySet()) {
        int subId = subcomponent.getKey();
        double quantity = subcomponent.getValue();

        if (operationToItemMap.containsKey(subId)) {
        // Subcomponent is an operation; resolve its associated item
        int resolvedItemId = operationToItemMap.get(subId);
        Operation subOperation = findOperationById(subId);
        Item resolvedItem = findItemById(resolvedItemId);

        // Create and link operation node if it hasn't been added yet
        if (!addedOperations.contains(subOperation.getId())) {
        ProductionTreeNode operationNode = new ProductionTreeNode(subOperation);
        node.addChild(operationNode);
        addedOperations.add(subOperation.getId());

        // Create and link resolved item node under the operation
        ProductionTreeNode resolvedItemNode = new ProductionTreeNode(resolvedItem);
        resolvedItemNode.setQuantity(quantity);
        operationNode.addChild(resolvedItemNode);

        // Recursively build the subtree for the resolved item
        buildSubTree(resolvedItemNode, visitedItems, addedOperations, operationToItemMap);
        }
        } else {
        // Subcomponent is a regular item
        Item subItem = findItemById(subId);
        ProductionTreeNode subNode = new ProductionTreeNode(subItem);
        subNode.setQuantity(quantity);

        node.addChild(subNode);

        // Recursively build the subtree for the sub-item
        buildSubTree(subNode, visitedItems, addedOperations, operationToItemMap);
        }
        }
        }
        }

``````

***Complexity Analysis:***

**Preventing infinite recursion:**
- `visitedItems` ensures each item is processed only once (O(1) per check/addition).

**Processing operations and subcomponents:**
- Each operation is retrieved and checked once using `findOperationByItemId` (O(p)) and `operationToItemMap` (O(1)).
- Subcomponents in `booData` are processed in a loop, and items/operations are recursively resolved and added (O(e)).

**Recursive calls:**
- Each item and operation is processed once, and recursion stops at leaf nodes.

**Total Complexity:**
- Let:
- i = number of items.
- p = number of operations.
- e = number of edges (relationships in `booData` and `operationToItemMap`).
- Total complexity: **O(i + p + e)**.

> **findItemById**

`````java
    private Item findItemById(int id) {
        return items.stream()
        .filter(item -> item.getId() == id)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Item not found for ID: " + id));
        }
``````

***Complexity Analysis:***

**Iteration:**
- Traverses the `items` list using `.stream()`.
- Worst-case: O(n), where n is the size of `items`.

**Filtering:**
- `item.getId() == id` is O(1) per item.

**Finding the first match:**
- `.findFirst()` stops early if a match is found (best-case O(1)).
- If no match, full traversal occurs (worst-case O(n)).

**Total Complexity:**
- Best case: **O(1)**.
- Worst case: **O(n)**.


> **findOperationByItemId**

`````java
    private Operation findOperationByItemId(int itemId, Map<Integer, Integer> operationToItemMap) {
        return operations.stream()
        .filter(operation -> operationToItemMap.get(operation.getId()) != null
        && operationToItemMap.get(operation.getId()) == itemId)
        .findFirst()
        .orElse(null); // Return null if no operation is found
        }
``````

***Complexity Analysis:***

**Iteration:**
- Traverses the `operations` list using `.stream()`.
- Worst-case: O(p), where p is the size of `operations`.

**Filtering:**
- Access to `operationToItemMap` is O(1) per operation (assuming HashMap).
- Combined filtering is O(1) per operation.

**Finding the first match:**
- `.findFirst()` stops early if a match is found (best-case O(1)).
- If no match, full traversal occurs (worst-case O(p)).

**Total Complexity:**
- Best case: **O(1)**.
- Worst case: **O(p)**.


> **findOperationById**

`````java
   private Operation findOperationById(int operationId) {
        return operations.stream()
        .filter(op -> op.getId() == operationId)
        .findFirst()
        .orElseThrow(() -> new IllegalArgumentException("Operation not found for ID: " + operationId));
        }

``````

***Complexity Analysis:***

**Iteration:**
- Traverses the `operations` list using `.stream()`.
- Worst-case: O(p), where p is the size of `operations`.

**Filtering:**
- `op.getId() == operationId` is O(1) per operation.

**Finding the first match:**
- `.findFirst()` stops early if a match is found (best-case O(1)).
- If no match, full traversal occurs (worst-case O(p)).

**Exception handling:**
- `.orElseThrow(...)` is executed only if no match is found and does not affect the search complexity.

**Total Complexity:**
- Best case: **O(1)**.
- Worst case: **O(p)**.

### USEI09

> **indexNode**

`````java
    private void indexNode(ProductionTreeNode node, Operation parentOperation) {
        // Index the current node by name and ID
        if (node.getItem() != null) {
        nameMap.put(node.getItem().getName(), node);
        idMap.put(String.valueOf(node.getItem().getId()), node);
        } else if (node.getOperation() != null) {
        nameMap.put(node.getOperation().getName(), node);
        idMap.put(String.valueOf(node.getOperation().getId()), node);
        }

        // Update the node's parent operation reference
        node.setParentOperation(parentOperation);

        // Recurse for children
        for (ProductionTreeNode child : node.getChildren()) {
        indexNode(child, node.getOperation() != null ? node.getOperation() : parentOperation);
        }
        }


``````
***Complexity Analysis:***

**Indexing the current node:**
- Accessing and updating `nameMap` and `idMap` are O(1) operations if these are HashMaps.
- Setting the parent operation (`node.setParentOperation`) is O(1).

**Recursion for children:**
- Each child node is visited once.
- If the tree has n nodes, this method visits all nodes exactly once.

**Total Complexity:**
- O(n), where n is the number of nodes in the tree.

> **search**

`````java
    public String search(String searchTerm) {
        // Search by ID first
        ProductionTreeNode nodeById = idMap.get(searchTerm);
        if (nodeById != null) {
        String title = "=== Search Result for ID: " + searchTerm + " ===\n";
        return title + getNodeDetails(nodeById);
        }

        // Search by Name
        ProductionTreeNode nodeByName = nameMap.get(searchTerm);
        if (nodeByName != null) {
        String title = "=== Search Result for Name: " + searchTerm + " ===\n";
        return title + getNodeDetails(nodeByName);
        }

        // If not found
        return "=== No results found for: " + searchTerm + " ===";
        }

``````
***Complexity Analysis:***

**Searching by ID:**
- `idMap.get(searchTerm)` is O(1) assuming `idMap` is a HashMap.

**Searching by Name:**
- If not found by ID, `nameMap.get(searchTerm)` is also O(1) for a HashMap.

**Total Complexity:**
- O(1), as both searches involve constant-time lookups.

> **getNodeDetails**

`````java
    private String getNodeDetails(ProductionTreeNode node) {
        StringBuilder details = new StringBuilder();

        if (node.getItem() != null) {
        details.append("Type: Material\n");
        details.append("Name: ").append(node.getItem().getName()).append("\n");
        details.append("ID: ").append(node.getItem().getId()).append("\n");
        details.append("Quantity: ").append(node.getQuantity()).append("\n");
        } else if (node.getOperation() != null) {
        details.append("Type: Operation\n");
        details.append("Name: ").append(node.getOperation().getName()).append("\n");
        details.append("ID: ").append(node.getOperation().getId()).append("\n");
        }

        if (node.getParentOperation() != null) {
        details.append("Parent Operation: [Op")
        .append(node.getParentOperation().getId())
        .append("] ")
        .append(node.getParentOperation().getName())
        .append("\n");
        }

        return details.toString();
        }

``````
***Complexity Analysis:***

**Building details:**
- Accessing properties of `node` (`getItem`, `getOperation`, etc.) is O(1).
- Appending to a `StringBuilder` is O(1) per operation.

**Total Complexity:**
- O(1), as the method only processes the properties of a single node.

> **getNodeByNameOrId**

`````java
    public ProductionTreeNode getNodeByNameOrId(String searchTerm) {
        ProductionTreeNode nodeByName = nameMap.get(searchTerm);
        if (nodeByName != null) {
        return nodeByName;
        }

        try {
        ProductionTreeNode nodeById = idMap.get(searchTerm);
        if (nodeById != null) {
        return nodeById;
        }
        } catch (NumberFormatException e) {

        }

        return null;
        }

``````

***Complexity Analysis:***

**Searching by Name:**
- `nameMap.get(searchTerm)` is O(1) for a HashMap.

**Searching by ID:**
- `idMap.get(searchTerm)` is also O(1).
- Catching `NumberFormatException` does not affect complexity.

**Total Complexity:**
- O(1), as both searches involve constant-time lookups.

### USEI10

> **insertRecursive**

`````java
        private Node insertRecursive(Node node, Double quantity, String material) {
        if (node == null) {
        return new Node(quantity, material);
        }

        if (quantity < node.quantity) {
        node.left = insertRecursive(node.left, quantity, material);
        } else if (quantity > node.quantity) {
        node.right = insertRecursive(node.right, quantity, material);
        } else {
        // Quantity already exists, add material to the list
        node.materials.add(material);
        }

        return node;
        }


``````
**Complexity Analysis:**

**Recursive Insertion:**
- Traverses the binary search tree (BST) to find the correct position for insertion.
- On a balanced BST, traversal depth is proportional to the tree height: O(log n), where n is the number of nodes.
- On an unbalanced BST, the worst-case traversal depth is O(n).

**Node Operations:**
- Creating a new node (O(1)).
- Adding a material to the node's list (O(1)).

**Total Complexity:**
- Best case: **O(log n)** (balanced BST).
- Worst case: **O(n)** (unbalanced BST).


> **displayInOrder**

`````java
    public void displayInOrder() {
        System.out.println("Materials in Increasing Order of Quantity:");
        displayInOrderRecursive(root);
        }

``````

***Complexity Analysis:***

**Tree Traversal:**
- Uses in-order traversal, visiting each node exactly once.
- Each node visit involves a constant-time operation (O(1)).

**Total Complexity:**
- O(n), where n is the number of nodes in the BST.

> **displayInOrderRecursive**
`````java
    private void displayInOrderRecursive(Node node) {
        if (node != null) {
        displayInOrderRecursive(node.left);
        System.out.println("Quantity: " + node.quantity + ", Materials: " + node.materials);
        displayInOrderRecursive(node.right);
        }
        }

``````

***Complexity Analysis:***

**Recursive Traversal:**
- Visits each node exactly once (left, root, right).
- At each node, a constant-time operation (O(1)) is performed to print the node's details.

**Total Complexity:**
- O(n), where n is the number of nodes in the BST.



> **displayTotalMaterialsTest**
`````java
    public void displayTotalMaterialsTest(){
        displayTotalMaterialsQuantityRecursive(root);
        }
``````
***Complexity Analysis:***

**Recursive Call:**
- Delegates the work to `displayTotalMaterialsQuantityRecursive`.
- Complexity matches that method: O(n), where n is the number of nodes.


> **displayTotalMaterialsQuantityRecursive**

`````java
    public double displayTotalMaterialsQuantityRecursive(Node node) {
        if (node == null) {
        return 0.0; // Base case: no quantity in a null node
        }

        // Traverse left, process current node, and then traverse right
        double leftSum = displayTotalMaterialsQuantityRecursive(node.left);

        // Calculate total materials for the current operation
        int totalMaterialsInNode = node.materials.size();
        System.out.println("Quantity: " + node.quantity
        + ", Materials: " + node.materials
        + "\nTotal Materials in Node: " + totalMaterialsInNode);

        double rightSum = displayTotalMaterialsQuantityRecursive(node.right);

        // Accumulate the sum of quantities
        return leftSum + node.quantity + rightSum;

        }


``````
***Complexity Analysis:***

**Recursive Traversal:**
- Visits each node exactly once (left, root, right).
- At each node:
- Calculating the total materials in the node is O(1).
- Printing details is also O(1).

**Total Complexity:**
- O(n), where n is the number of nodes in the BST.

> **displayInReverseOrder**
`````java
    public void displayInReverseOrder() {
        System.out.println("Materials in Decreasing Order of Quantity:");
        displayInReverseOrderRecursive(root);
        }
``````
***Complexity Analysis:***

**Tree Traversal:**
- Uses reverse in-order traversal, visiting each node exactly once (right, root, left).
- Each node visit involves a constant-time operation (O(1)).

**Total Complexity:**
- O(n), where n is the number of nodes in the BST.

> **displayInReverseOrderRecursive**

`````java
    private void displayInReverseOrderRecursive(Node node) {
        if (node != null) {
        displayInReverseOrderRecursive(node.right);
        System.out.println("Quantity: " + node.quantity + ", Materials: " + node.materials);
        displayInReverseOrderRecursive(node.left);
        }
        }
``````
***Complexity Analysis:***
**Recursive Traversal:**
- Visits each node exactly once (right, root, left).
- At each node, a constant-time operation (O(1)) is performed to print the node's details.

**Total Complexity:**
- O(n), where n is the number of nodes in the BST.

> **updateMaterialQuantite**

`````java
  public void updateMaterialQuantity(String materialName, double newQuantity) {
        root = updateMaterialRecursive(root, materialName, newQuantity);
        }

``````
***Complexity Analysis:***

**Recursive Search:**
- Traverses the BST to locate the node containing the material.
- On a balanced BST, traversal depth is O(log n).
- On an unbalanced BST, worst-case traversal depth is O(n).

**Updating a Node:**
- Checking and updating a material in a node's list is O(1).

**Total Complexity:**
- Best case: **O(log n)** (balanced BST).
- Worst case: **O(n)** (unbalanced BST).

> **updateMaterialRecursive**

`````java
    private Node updateMaterialRecursive(Node node, String materialName, double newQuantity) {
        if (node == null) {
        return null; // Material not found
        }


        // Find the material in the node's list
        if (node.materials.contains(materialName)) {
        // Update the material's quantity
        node.quantity = newQuantity;
        System.out.println(newQuantity);
        } else if (materialName.compareTo(node.materials.get(0)) < 0) {
        // Search left if the material name is lexicographically smaller
        node.left = updateMaterialRecursive(node.left, materialName, newQuantity);
        } else {
        // Search right if the material name is lexicographically larger
        node.right = updateMaterialRecursive(node.right, materialName, newQuantity);
        }

        return node;
        }

``````
***Complexity Analysis:***

**Recursive Search:**
- Traverses the BST to locate the node containing the material.
- On a balanced BST, traversal depth is O(log n).
- On an unbalanced BST, worst-case traversal depth is O(n).

**Updating a Node:**
- Checking for a material in a node's list is O(1).
- Updating the node's quantity is O(1).

**Total Complexity:**
- Best case: **O(log n)** (balanced BST).
- Worst case: **O(n)** (unbalanced BST).


### USEI11

> **insertRecursive**


`````java
public record QualityCheck(int checkId, String checkName, int priorityLevel) implements Comparable<QualityCheck> {

    // Implement the compareTo method to define the priority
    @Override
    public int compareTo(QualityCheck other) {
        // Higher priority level should come first (max-heap)
        return Integer.compare(other.priorityLevel, this.priorityLevel);
    }

    @Override
    public String toString() {
        return String.format(
                "Quality Check [Priority Level: %d, ID: %d, Name: '%s']",
                priorityLevel, checkId, checkName
        );
    }
}


``````
***Complexity Analysis:***

**compareTo Method:**
- Compares the `priorityLevel` of two `QualityCheck` objects.
- Uses `Integer.compare`, which is O(1).
- Total complexity: **O(1)**.

**toString Method:**
- Formats the `QualityCheck` object into a string representation using `String.format`.
- The number of placeholders in the format string is fixed, so formatting is O(1).
- Total complexity: **O(1)**.

**Usage in Data Structures:**
- The `compareTo` method may be used in sorting or priority-based data structures (e.g., `PriorityQueue`).
- For sorting an array or list of `QualityCheck` objects, the `compareTo` method is called once per comparison:
- Sorting complexity: **O(n log n)** for `n` objects in an efficient sorting algorithm (e.g., merge sort, quicksort).
- For a priority queue (heap):
- Insertion complexity: **O(log n)**.
- Extraction complexity: **O(log n)**.


> **addQualityCheckBasedOnDepth**

`````java
    public void addQualityCheckBasedOnDepth(ProductionTreeNode node, int depth) {
        if (node == null) return;

        // Assign higher priority for closer operations (smaller depth = higher priority)
        if(node.getItemId() != -1) {
        String checkName = node.getItemName() + " (" + node.getItemId() + ")";

        // Create a QualityCheck instance for this operation
        QualityCheck qualityCheck = new QualityCheck(node.getItemId(), checkName, depth);

        // Add the quality check to the priority queue
        qualityCheckQueue.offer(qualityCheck);
        }


        // Recursively add the operations for the child nodes with incremented depth
        for (ProductionTreeNode childNode : node.getChildren()) {
        addQualityCheckBasedOnDepth(childNode, depth + 1); // Increase depth as we go down the tree
        }
        }

``````

***Complexity Analysis:***

**Recursive Tree Traversal:**
- Visits each node in the tree once.
- If the tree has n nodes, the traversal is O(n).

**Quality Check Creation and Insertion:**
- For each node, creates a `QualityCheck` (O(1)).
- Inserts the `QualityCheck` into the priority queue, which is O(log k), where k is the current number of elements in the queue.

**Total Complexity:**
- Traversal: O(n).
- Insertion: O(n log n) for all nodes.
- Total: **O(n log n)**.


>**processQualityChecksInRevers**

`````java
public void processQualityChecksInReverse() {
        // Create a stack to reverse the order
        Stack<QualityCheck> reverseStack = new Stack<>();

        // Move all quality checks to the stack
        while (!qualityCheckQueue.isEmpty()) {
        reverseStack.push(qualityCheckQueue.poll());
        }

        // Now process and perform quality checks in reverse order (lowest priority first)
        while (!reverseStack.isEmpty()) {
        QualityCheck qc = reverseStack.pop();  // Pop the checks from the stack (which gives reverse order)
        System.out.println("Performing Quality Check: " + qc);
        }
        }
``````

***Complexity Analysis:***

**Reversing the Priority Queue:**
- Moving all elements from the priority queue to a stack:
- Removing elements from the queue is O(log n) per element.
- Pushing elements to the stack is O(1) per element.
- Total for n elements: O(n log n).

**Processing in Reverse Order:**
- Popping elements from the stack is O(1) per element.
- Total for n elements: O(n).

**Total Complexity:**
- Reversing: O(n log n).
- Processing: O(n).
- Total: **O(n log n)**.

>**performQualityChecks**

`````java
    public void performQualityChecks() {
        while (!qualityCheckQueue.isEmpty()) {
        QualityCheck check = qualityCheckQueue.poll();
        System.out.println("Performing Quality Check: " + check);
        }
        }
``````

***Complexity Analysis:***

**Processing the Priority Queue:**
- Removes elements from the priority queue one at a time.
- Each removal is O(log n).
- For n elements, the total complexity is **O(n log n)**.

>**viewQualityChecks**

`````java
    public void viewQualityChecks() {
        System.out.println("Quality Checks in Order of Priority:");
        for (QualityCheck check : qualityCheckQueue) {
        System.out.println(check);
        }
        }
``````

***Complexity Analysis:***

**Iterating through the Priority Queue:**
- Iterating over the elements of the priority queue is O(n), where n is the size of the queue.
- Printing each element is O(1) per element.

**Total Complexity:**
- **O(n)**.

### USEI12

> **updateMaterialQuantity**

```java
public void updateMaterialQuantity(double newQuantity) {
    double scaleFactor = newQuantity / this.quantity; // Calculate the scale factor
    this.quantity = newQuantity; // Update the current node's quantity

    // Update quantities for all child nodes based on the scale factor
    for (ProductionTreeNode child : children) {
        double updatedQuantity = child.getQuantity() * scaleFactor;
        child.updateMaterialQuantity(updatedQuantity);
    }
}
```

***Complexity Analysis:***

**Recursive Update of Quantities:**
- The method visits each node in the tree once.
- For each node, it performs a constant number of operations (e.g., calculating the scale factor, updating quantities).
- If the tree has `n` nodes, the traversal complexity is **O(n)**.

**Total Complexity:**
- **O(n)**, where `n` is the number of nodes in the tree.

---

> **updateItemQuantity**

```java
public void updateItemQuantity(int itemId, double newQuantity) {

    // Update the itemQuantities map
    if (itemQuantities.containsKey(itemId)) {
        itemQuantities.put(itemId, newQuantity);
    }

    // Update booData where this item ID exists as a child or parent
    if (booData.containsKey(itemId)) {
        for (Map.Entry<Integer, Double> entry : booData.get(itemId).entrySet()) {
            entry.setValue(newQuantity);
        }
    }

    for (Map.Entry<Integer, Map<Integer, Double>> entry : booData.entrySet()) {
        if (entry.getValue().containsKey(itemId)) {
            entry.getValue().put(itemId, newQuantity);
        }
    }

    System.out.println("Updated booData: " + booData);

}
```

***Complexity Analysis:***

**Updating `itemQuantities`:**
- Checking and updating the map is **O(1)** for a hash map.

**Updating `booData`:**
- Iterates over `booData` to update relevant entries:
    - First loop: Accessing `booData.get(itemId)` is **O(1)**, but iterating over its entries is **O(m)**, where `m` is the number of child entries for `itemId`.
    - Second loop: Iterates over all `booData` entries, which is **O(k)**, where `k` is the total number of entries in the map.

**Total Complexity:**
- Let `k` be the size of `booData` and `m` the average number of child entries for a given item.
- Total complexity is approximately **O(k + m)** in the worst case.

---

> **updateMaterialRecursive**

```java
private Node updateMaterialRecursive(Node node, String materialName, double newQuantity) {
    if (node == null) {
        return null; // Material not found
    }

    if (node.materials.contains(materialName)) {
        node.quantity = newQuantity; // Update the quantity
        System.out.println("Updated quantity to: " + newQuantity);
    } else if (materialName.compareTo(node.materials.get(0)) < 0) {
        node.left = updateMaterialRecursive(node.left, materialName, newQuantity);
    } else {
        node.right = updateMaterialRecursive(node.right, materialName, newQuantity);
    }

    return node;
}
```

***Complexity Analysis:***

**Recursive Search and Update:**
- The method follows the structure of a binary search tree.
- In the worst case, it traverses from the root to a leaf node.
- If the tree is balanced, this traversal is **O(log n)**, where `n` is the number of nodes.
- If the tree is unbalanced (degenerates into a linked list), this traversal is **O(n)**.

**String Comparisons:**
- The string comparison operation (`compareTo`) is **O(s)**, where `s` is the length of the material name.
- In practice, `s` is much smaller than `n`, so it does not dominate the overall complexity.

**Total Complexity:**
- **O(log n)** for a balanced tree.
- **O(n)** for an unbalanced tree.

### USEI13

> **displayTotalMaterialsTest**

```java
public void displayTotalMaterialsTest() {
    double totalQuantity = displayTotalMaterialsQuantityRecursive(root);
    System.out.println("Total Quantity of Materials Used: " + totalQuantity);
}

private double displayTotalMaterialsQuantityRecursive(Node node) {
    double total = 0.0;

    if (node == null) {
        return total;
    }

    total += displayTotalMaterialsQuantityRecursive(node.left);

    int totalMaterialsInNode = node.materials.size();
    System.out.println("Quantity: " + node.quantity
            + ", Materials: " + node.materials
            + "\nTotal Materials in Node: " + totalMaterialsInNode);

    total += node.quantity;

    total += displayTotalMaterialsQuantityRecursive(node.right);

    return total;
}
```

***Complexity Analysis:***

**Recursive Traversal:**
- The method traverses each node in the binary tree exactly once.
- For a tree with `n` nodes, the traversal complexity is **O(n)**.

**Per-Node Operations:**
- At each node, the size of the materials list (`node.materials.size()`) is accessed, which is **O(1)** as it only retrieves the size.
- Printing statements involve constant-time operations.

**Total Complexity:**
- Since the traversal dominates, the total complexity is **O(n)**, where `n` is the number of nodes in the binary tree.


### USEI14

> **criticalPathOperations**

```java
public static void criticalPathOperations(List<Item> items, List<Operation> operations, BooDataResult booDataResult) {
    // Create a map to store the mapping between operation ID and item ID
    Map<Integer, Integer> operationToItemMap = new HashMap<>();

    // Creating the tree builder that will be used to build operation trees
    ProductionTreeBuilderOpID treeBuilder = new ProductionTreeBuilderOpID(items, operations, booDataResult.booData, booDataResult.itemQuantities);

    // Map to store operations and their respective tree depths
    Map<Operation, Integer> operationDepths = new HashMap<>();

    // Calculate the depth of each tree by iterating over each operation
    for (Operation operation : operations) {
        // Build the tree for each operation
        ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
        // Calculate the depth of the current operation's tree
        int depth = calculateTreeDepth(rootNode);
        // Store the calculated depth for the operation
        operationDepths.put(operation, depth);
    }

    // Using PriorityQueue to store operations ordered by tree depth (most critical first)
    PriorityQueue<Operation> priorityQueue = new PriorityQueue<>(
            (op1, op2) -> Integer.compare(operationDepths.get(op2), operationDepths.get(op1))
    );

    // Add all operations to the priority queue
    priorityQueue.addAll(operations);

    // Process operations in the order of their criticality (based on tree depth)
    ProductionTreePrinter printer = new ProductionTreePrinter(booDataResult.booData);
    while (!priorityQueue.isEmpty()) {
        // Poll the operation with the highest priority (most critical)
        Operation operation = priorityQueue.poll();
        // Print the critical path operation details
        System.out.println("\nCritical Path Operation for: " + operation.getName() + " (ID: " + operation.getId() + ")");
        // Build the operation tree for this operation
        ProductionTreeNode rootNode = treeBuilder.buildTree(operation.getId(), operationToItemMap);
        // Print the operation tree
        printer.printOperationTree(rootNode);
    }
}
```

***Complexity Analysis:***

**Tree Building for Each Operation:**
- For each operation, the tree is built using `treeBuilder.buildTree`.
- Let `t` be the average complexity of building a tree. If this involves visiting all nodes/items related to the operation, it can scale up to **O(n)** for `n` nodes.

**Calculating Tree Depth:**
- The `calculateTreeDepth` method is called for each operation's tree.
- For a tree with `d` nodes, this method traverses all nodes, resulting in **O(d)** per tree.
- Total for all operations: **O(m × d)**, where `m` is the number of operations and `d` the average tree size.

**Priority Queue Operations:**
- Adding `m` operations to the priority queue: **O(m log m)**.
- Removing all `m` operations from the queue: **O(m log m)**.

**Tree Printing:**
- Printing involves traversing and formatting the tree. For a tree with `d` nodes, this is **O(d)** per tree.

**Total Complexity:**
- Tree building and depth calculation: **O(m × d)**.
- Priority queue operations: **O(m log m)**.
- Tree printing: **O(m × d)**.
- Combined total complexity: **O(m × d + m log m)**.

---

> **calculateTreeDepth**

```java
public static int calculateTreeDepth(ProductionTreeNode node) {
    // Base case: if the node is null or has no children, the depth is 1
    if (node == null || node.getChildren().isEmpty()) {
        return 1;
    }

    // Recursively calculate the depth of each child and return the maximum depth plus 1
    int maxDepth = 0;
    for (ProductionTreeNode child : node.getChildren()) {
        maxDepth = Math.max(maxDepth, calculateTreeDepth(child));
    }
    return maxDepth + 1;
}
```

***Complexity Analysis:***

**Recursive Tree Depth Calculation:**
- Visits each node in the tree once.
- For a tree with `n` nodes, the traversal complexity is **O(n)**.

**Per-Node Operations:**
- The `getChildren` call retrieves the child list, which is typically **O(1)**.
- `Math.max` and a simple loop to iterate over children are also constant-time for each child.

**Total Complexity:**
- **O(n)**, where `n` is the number of nodes in the tree.




