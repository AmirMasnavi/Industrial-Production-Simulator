#include "machine.h"
#include <stdlib.h>
#include <string.h>
#include <stdio.h>
#include "operation.h"

Machine* setup_machine(int id, const char* name, float temp_min, float temp_max, float hum_min, float hum_max, int buffer_length, int median_window_length) {
    // Validate parameters
    if (buffer_length <= 0 || median_window_length <= 0) {
        fprintf(stderr, "Error: Invalid buffer or median window length\n");
        return NULL;
    }

    // Allocate memory for a Machine
    Machine* machine = (Machine*)malloc(sizeof(Machine));
    if (!machine) {
        perror("Failed to allocate memory for machine");
        return NULL;
    }

    // Initialize the machine properties
    machine->id = id;
    strncpy(machine->name, name, sizeof(machine->name) - 1);
    machine->name[sizeof(machine->name) - 1] = '\0';
    machine->temp_min = temp_min;
    machine->temp_max = temp_max;
    machine->hum_min = hum_min;
    machine->hum_max = hum_max;
    machine->buffer_length = buffer_length;
    machine->median_window_length = median_window_length;
    machine->state = ON;
    machine->head = 0;
    machine->tail = 0;
    machine->operations = NULL;

    // Dynamically allocate the circular buffer
    machine->circular_buffer = (int*)malloc(buffer_length * sizeof(int));
    if (!machine->circular_buffer) {
        perror("Failed to allocate memory for circular buffer");
        free(machine);
        return NULL;
    }

    // Initialize the buffer to zero
    memset(machine->circular_buffer, 0, buffer_length * sizeof(int));

    return machine;
}

void free_machine(Machine* machine) {
    if (!machine) return;

    // Free the circular buffer
    if (machine->circular_buffer) {
        free(machine->circular_buffer);
    }

    // Free the machine struct itself
    free(machine);
}

// Função para carregar máquinas do arquivo
int loadMachines(const char *filename, Machine *machines, int maxMachines) {
    FILE *file = fopen(filename, "r");
    if (!file) {
        printf("Failed to open file: %s\n", filename);
        return -1;
    }

    int count = 0;
    while (count < maxMachines && fscanf(file, "%d,%49[^,],%f,%f,%f,%f",
                                         &machines[count].id,
                                         machines[count].name,
                                         &machines[count].temp_min,
                                         &machines[count].temp_max,
                                         &machines[count].hum_min,
                                         &machines[count].hum_max) == 6) {
        count++;
    }

    fclose(file);
    return count;
}

// Função para encontrar uma máquina pelo ID
Machine *findMachineByID(Machine *machines, int count, int id) {
    for (int i = 0; i < count; i++) {
        if (machines[i].id == id) {
            return &machines[i];
        }
    }
    return NULL;
}

// Função para verificar alertas
void checkAlerts(Machine *machine, float temp, float hum) {
    if (temp < machine->temp_min || temp > machine->temp_max) {
        printf("ALERT: Temperature (%.2f) out of bounds for machine %s! (%.2f - %.2f)\n",
               temp, machine->name, machine->temp_min, machine->temp_max);
    }

    if (hum < machine->hum_min || hum > machine->hum_max) {
        printf("ALERT: Humidity (%.2f) out of bounds for machine %s! (%.2f - %.2f)\n",
               hum, machine->name, machine->hum_min, machine->hum_max);
    }
}




int setup_machines_from_file(const char* filename, Machine*** machines) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        perror("Failed to open file");
        return -1;
    }

    char line[256];
    int machine_count = 0;

    // Allocate initial space for machines
    Machine** machine_list = NULL;

    while (fgets(line, sizeof(line), file)) {
        int id, buffer_length, median_window_length;
        char name[50];
        float temp_min, temp_max, hum_min, hum_max;

        // Parse the line
        if (sscanf(line, "%d,%49[^,],%f,%f,%f,%f,%d,%d",
                   &id, name, &temp_min, &temp_max, &hum_min, &hum_max, &buffer_length, &median_window_length) != 8) {
            fprintf(stderr, "Error: Invalid line format - %s", line);
            continue;
        }

        // Create and initialize the machine
        Machine* machine = setup_machine(id, name, temp_min, temp_max, hum_min, hum_max, buffer_length, median_window_length);
        if (!machine) {
            fprintf(stderr, "Error: Failed to initialize machine for line - %s", line);
            continue;
        }

        // Resize the machine list
        machine_list = realloc(machine_list, (machine_count + 1) * sizeof(Machine*));
        if (!machine_list) {
            perror("Failed to reallocate memory for machine list");
            free_machine(machine);
            fclose(file);
            return -1;
        }

        machine_list[machine_count++] = machine;
    }

    fclose(file);
    *machines = machine_list;
    return machine_count;
}
