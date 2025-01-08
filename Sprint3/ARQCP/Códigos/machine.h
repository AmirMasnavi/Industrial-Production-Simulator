#ifndef MACHINE_H
#define MACHINE_H

#include <time.h>  // For timestamp handling

#include "operation.h"

typedef enum { OFF, ON, OP } MachineState;

typedef struct Machine {
    int id;
    char name[50];
    float temp_min;
    float temp_max;
    float hum_min;
    float hum_max;
    int buffer_length;
    MachineState state;
    int median_window_length;
    int* circular_buffer;  // Circular buffer for data
    Operation* operations;  // Pointer to Operation
    int operation_count;    // Number of operations
    int head;
    int tail;
} Machine;

// Function declarations
Machine* setup_machine(int id, const char* name, float temp_min, float temp_max, float hum_min, float hum_max, int buffer_length, int median_window_length);
void free_machine(Machine* machine);
int setup_machines_from_file(const char* filename, Machine*** machines);
int export_machine_operations(Machine *machine, const char *filename);
int loadMachines(const char *filename, Machine *machines, int maxMachines);
Machine *findMachineByID(Machine *machines, int count, int id);
void checkAlerts(Machine *machine, float temp, float hum);

#endif // MACHINE_H
