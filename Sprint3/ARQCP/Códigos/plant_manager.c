#include "plant_manager.h"
#include "machine.h"  // Include Machine structure and functions
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "operation.h"
#include <stdbool.h>


// Create a PlantFloorManager
PlantFloorManager* create_plant_manager(int capacity) {
    PlantFloorManager* manager = (PlantFloorManager*)malloc(sizeof(PlantFloorManager));
    if (!manager) {
        perror("Failed to allocate memory for PlantFloorManager");
        return NULL;
    }

    manager->machines = (Machine**)malloc(capacity * sizeof(Machine*));
    manager->states = (char**)malloc(capacity * sizeof(char*));
    if (!manager->machines || !manager->states) {
        perror("Failed to allocate memory for arrays");
        free(manager->machines);
        free(manager->states);
        free(manager);
        return NULL;
    }

    for (int i = 0; i < capacity; i++) {
        manager->states[i] = NULL;  // Initialize states as NULL
    }

    manager->count = 0;
    manager->capacity = capacity;
    return manager;
}

// Free the PlantFloorManager
void free_plant_manager(PlantFloorManager* manager) {
    if (manager) {
        for (int i = 0; i < manager->count; i++) {
            free_machine(manager->machines[i]);
            free(manager->states[i]);  // Free individual state strings
        }
        free(manager->machines);
        free(manager->states);
        free(manager);
    }
}

bool validate_state_transition(MachineState current_state, MachineState new_state) {
    // Proibir transições inválidas
    if (current_state == OFF && new_state == OP) {
        printf("Error: Cannot transition from OFF to OP.\n");
        return false;
    }
    if (current_state == OP && new_state == OFF) {
        printf("Error: Cannot transition from OP to OFF directly.\n");
        return false;
    }

    // Permitir todas as outras transições
    return true;
}



Machine *get_machine_by_id(PlantFloorManager *manager, int id) {
    for (int i = 0; i < manager->count; i++) {
        if (manager->machines[i]->id == id) {
            return manager->machines[i];
        }
    }
    return NULL; // Machine not found
}

// Add a machine to the plant floor
int add_machine(PlantFloorManager* manager, Machine* machine) {
    if (manager->count >= manager->capacity) {
        fprintf(stderr, "Plant floor capacity exceeded\n");
        return -1;
    }

    manager->machines[manager->count] = machine;
    manager->states[manager->count] = strdup("ON");// Default state is "OFF"
    manager->count++;
    printf("Machine %d (%s) added successfully\n", machine->id, machine->name);
    return 0;
}

// Remove a machine from the plant floor
int remove_machine(PlantFloorManager* manager, int machine_id) {
    for (int i = 0; i < manager->count; i++) {
        if (manager->machines[i]->id == machine_id) {
            if (strcmp(manager->states[i], "OP") == 0) {
                fprintf(stderr, "Cannot remove Machine %d; it is currently operating\n", machine_id);
                return -1;
            }

            printf("Removing Machine %d (%s)\n", manager->machines[i]->id, manager->machines[i]->name);
            free_machine(manager->machines[i]);
            free(manager->states[i]);

            // Shift the remaining machines and states
            for (int j = i; j < manager->count - 1; j++) {
                manager->machines[j] = manager->machines[j + 1];
                manager->states[j] = manager->states[j + 1];
            }

            manager->count--;
            return 0;
        }
    }

    fprintf(stderr, "Machine %d not found\n", machine_id);
    return -1;
}

// Get the state of a machine on the plant floor
char* get_machine_state(PlantFloorManager* manager, int machine_id) {
    for (int i = 0; i < manager->count; i++) {
        if (manager->machines[i]->id == machine_id) {
            return manager->states[i];
        }
    }

    fprintf(stderr, "Machine %d not found\n", machine_id);
    return NULL;
}

// Set the state of a machine
int set_machine_state(PlantFloorManager* manager, int machine_id, const char* state) {
    // Look for the machine with the given ID
    for (int i = 0; i < manager->count; i++) {
        if (manager->machines[i]->id == machine_id) {
            Machine* machine = manager->machines[i];

            // Map the state string to MachineState enum
            MachineState new_state;
            if (strcmp(state, "ON") == 0) {
                new_state = ON;
            } else if (strcmp(state, "OFF") == 0) {
                new_state = OFF;
            } else if (strcmp(state, "OP") == 0) {
                new_state = OP;
            } else {
                printf("Error: Invalid state '%s'.\n", state);
                return -1; // Invalid state
            }

            // Validate the state transition
            if (!validate_state_transition(machine->state, new_state)) {
                printf("Error: Invalid state transition from %s to %s.\n",
                       (machine->state == OFF ? "OFF" :
                        machine->state == ON ? "ON" : "OP"),
                       state);
                return -1; // Invalid transition
            }

            // Update the state of the machine
            machine->state = new_state;
            strncpy(manager->states[i], state, sizeof(manager->states[i]) - 1);

            // Create an operation to record the state change
            Operation op;
            strncpy(op.state, state, sizeof(op.state) - 1);
            strncpy(op.designation, "state_change", sizeof(op.designation) - 1); // Generic designation
            op.number = 0; // Generic number for state changes
            op.timestamp = time(NULL); // Current timestamp
            op.temperature = 0.0; // Default temperature
            op.humidity = 0.0;    // Default humidity

            // Write the operation to the CSV
            write_machine_operations_to_csv("machine1_operations.csv", &op, 1);

            return 0; // Success
        }
    }

    return -1; // Machine not found
}

//assign an operation to a machine
int assign_operation_to_machine(PlantFloorManager* manager, int machine_id, const char* operation_name, int operation_number) {
    // Validate operation number
    if (operation_number < 0 || operation_number > 31) {
        printf("Error: Operation number must be between 0 and 31.\n");
        return -1; // Invalid operation number
    }

    Machine* machine = get_machine_by_id(manager, machine_id);
    if (!machine) {
        printf("Error: Machine with ID %d not found.\n", machine_id);
        return -1; // Error: machine not found
    }

    // Create a new operation
    Operation new_operation;
    strncpy(new_operation.state, "OP", sizeof(new_operation.state) - 1); // Default state "OP"
    new_operation.state[sizeof(new_operation.state) - 1] = '\0';         // Ensure null-termination

    strncpy(new_operation.designation, operation_name, sizeof(new_operation.designation) - 1);
    new_operation.designation[sizeof(new_operation.designation) - 1] = '\0'; // Ensure null-termination

    new_operation.number = operation_number;
    new_operation.timestamp = time(NULL); // Current timestamp
    new_operation.temperature = 25.0;    // Default temperature
    new_operation.humidity = 50.0;       // Default humidity

    // Check if we need to resize the operations array
    machine->operations = realloc(machine->operations, (machine->operation_count + 1) * sizeof(Operation));
    if (!machine->operations) {
        perror("Failed to allocate memory for operations");
        return -1; // Memory allocation failed
    }

    // Add the operation to the machine
    machine->operations[machine->operation_count] = new_operation;
    machine->operation_count++; // Increment the operation count

    printf("Operation '%s' (Number: %d) assigned to machine '%s' successfully.\n", operation_name, operation_number, machine->name);
    return 0; // Success
}
