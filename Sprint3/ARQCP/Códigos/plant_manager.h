#ifndef PLANT_MANAGER_H
#define PLANT_MANAGER_H

#include "machine.h"  // Include the Machine structure
#include "operation.h" // Include the Operation structure
#include <stdbool.h>


// Structure to manage the plant floor
typedef struct {
    Machine** machines;    // Array of pointers to machines
    char** states;         // Array of strings representing the states of the machines
    int count;             // Number of machines currently on the plant floor
    int capacity;          // Total capacity of the arrays
} PlantFloorManager;


PlantFloorManager* create_plant_manager(int capacity); // Create a new plant manager
void free_plant_manager(PlantFloorManager* manager); // Free memory allocated to the plant manager
int add_machine(PlantFloorManager* manager, Machine* machine); // Add a machine to the plant floor
int remove_machine(PlantFloorManager* manager, int machine_id); // Remove a machine from the plant floor
char* get_machine_state(PlantFloorManager* manager, int machine_id); // Get the state of a specific machine
int set_machine_state(PlantFloorManager* manager, int machine_id, const char* state); // Set the state of a specific machine


int assign_operation_to_machine(PlantFloorManager* manager, int machine_id, const char* operation_name, int operation_number);

Machine *get_machine_by_id(PlantFloorManager *manager, int id);
int export_machine_operations(Machine *machine, const char *filename);
bool validate_state_transition(MachineState current_state, MachineState new_state);

#endif
