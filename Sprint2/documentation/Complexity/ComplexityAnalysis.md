# Complexity

## Complexity Analysis:

### USEI08
> **buildTre**

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


