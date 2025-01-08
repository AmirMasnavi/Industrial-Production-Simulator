#ifndef OPERATION_H
#define OPERATION_H

#include <time.h> // Para o timestamp


typedef struct {
    char state[4];         // "OP", "ON", "OFF"
    char designation[20];  // Designação da operação
    int number;            // Número da operação (0-31)
    time_t timestamp;      // Timestamp
    float temperature;     // Temperatura
    float humidity;        // Humidade
} Operation;

// Function prototype to write operations to a CSV file
void write_machine_operations_to_csv(const char* filename, Operation* operations, int operation_count);

#endif // OPERATION_H
