package org.example;

import java.io.*;
import java.util.*;

/**
 * Simulador que processa itens com base em uma árvore de dependências de operações.
 * Utiliza uma BST/AVL para organizar as operações conforme os níveis de dependência.
 */
public class SimulatorTree {
    private final Map<Integer, String> itemNames; // ID -> Nome do item
    private final Map<Integer, Operation> operations; // ID -> Operação
    private final Map<Integer, List<DependencyNode>> operationDependencies; // ID Operação -> Dependências
    private final List<Machine> machines; // Máquinas disponíveis
    private final MaterialBST materialInventory; // Inventário de materiais

    /**
     * Construtor principal que carrega os dados dos ficheiros CSV e inicializa o simulador.
     *
     * @param itemsFile       Caminho para o ficheiro items.csv
     * @param operationsFile  Caminho para o ficheiro operations.csv
     * @param booFile         Caminho para o ficheiro boo.csv
     * @param machinesFile    Caminho para o ficheiro workstations_v2.csv
     */
    public SimulatorTree(String itemsFile, String operationsFile, String booFile, String machinesFile) {
        this.itemNames = new HashMap<>();
        this.operations = new HashMap<>();
        this.operationDependencies = new HashMap<>();
        this.machines = new ArrayList<>();
        this.materialInventory = new MaterialBST();

        loadItems(itemsFile);
        loadOperations(operationsFile);
        loadBOO(booFile);
        loadMachines(machinesFile);
    }

    // Método para carregar os dados do ficheiro items.csv
    private void loadItems(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                itemNames.put(id, name);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar items.csv: " + e.getMessage());
        }
    }

    // Método para carregar os dados do ficheiro operations.csv
    private void loadOperations(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                int id = Integer.parseInt(fields[0].trim());
                String name = fields[1].trim();
                operations.put(id, new Operation(id, name));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar operations.csv: " + e.getMessage());
        }
    }

    // Método para carregar os dados do ficheiro boo.csv e construir dependências
    private void loadBOO(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(";");
                int operationId = Integer.parseInt(fields[0].trim());
                int itemId = Integer.parseInt(fields[1].trim());
                double itemQty = Double.parseDouble(fields[2].trim());

                DependencyNode node = new DependencyNode(operationId, itemId, itemQty);

                // Processar dependências de operações
                int depIndexStart = line.indexOf("(;");
                if (depIndexStart != -1) {
                    String dependencies = line.substring(depIndexStart + 2, line.lastIndexOf(");"));
                    String[] depFields = dependencies.split(";");
                    for (int i = 0; i < depFields.length; i += 2) {
                        if (!depFields[i].isBlank()) {
                            int depOpId = Integer.parseInt(depFields[i].trim());
                            int depQty = Integer.parseInt(depFields[i + 1].trim());
                            node.addDependency(depOpId, depQty);
                        }
                    }
                }

                operationDependencies.computeIfAbsent(operationId, k -> new ArrayList<>()).add(node);
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar boo.csv: " + e.getMessage());
        }
    }

    // Método para carregar as máquinas do ficheiro workstations_v2.csv
    private void loadMachines(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                machines.add(Machine.fromCSV(line));
            }
        } catch (IOException e) {
            throw new RuntimeException("Erro ao carregar workstations_v2.csv: " + e.getMessage());
        }
    }

    // Método principal de execução da simulação
    public void runSimulation() {
        System.out.println("Iniciando simulação...");
        // Implementar a lógica de execução por nível de dependência
    }

    /**
     * Classe auxiliar que representa um nó de dependência.
     */
    private static class DependencyNode {
        int operationId;
        int itemId;
        double itemQty;
        Map<Integer, Integer> dependencies; // Operação -> Quantidade necessária

        public DependencyNode(int operationId, int itemId, double itemQty) {
            this.operationId = operationId;
            this.itemId = itemId;
            this.itemQty = itemQty;
            this.dependencies = new HashMap<>();
        }

        public void addDependency(int operationId, int quantity) {
            dependencies.put(operationId, quantity);
        }

        @Override
        public String toString() {
            return "DependencyNode{" +
                    "operationId=" + operationId +
                    ", itemId=" + itemId +
                    ", itemQty=" + itemQty +
                    ", dependencies=" + dependencies +
                    '}';
        }
    }
}
