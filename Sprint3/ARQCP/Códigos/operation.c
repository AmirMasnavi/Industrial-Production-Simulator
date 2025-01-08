#include "operation.h"
#include "binaryNumber.h" // Include the header for get_number_binary
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <time.h> // To handle timestamps

void write_machine_operations_to_csv(const char* filename, Operation* operations, int operation_count) {
    // Open the file in append mode
    FILE* file = fopen(filename, "a");
    if (!file) {
        perror("Error opening file");
        return;
    }

    // Write the header only if the file is empty
    fseek(file, 0, SEEK_END);
    if (ftell(file) == 0) {
        fprintf(file, "State,Name,Number,Timestamp, Binary Number\n");
    }

    // Write each operation to the file
    for (int i = 0; i < operation_count; i++) {
        Operation op = operations[i];

        // Generate a valid timestamp
        char time_str[20];
        if (op.timestamp != 0) {
            struct tm* time_info = localtime(&op.timestamp);
            strftime(time_str, sizeof(time_str), "%Y-%m-%d %H:%M:%S", time_info);
        } else {
            strcpy(time_str, "0000-00-00 00:00:00");
        }

        // Convert the operation number to binary
        char binary_number[6] = {0}; // 5 bits + null terminator
        if (get_number_binary(op.number, binary_number) < 0) {
            strcpy(binary_number, "ERROR"); // Handle error case
        }
        // Reverse the binary number (it is stored in reverse order by get_number_binary)
        char reversed_binary[6] = {0}; // To store the reversed binary string
        for (int j = 0; j < 5; j++) {
            reversed_binary[j] = binary_number[4 - j]; // Reverse the order
        }

        // Format the binary number as a comma-separated string
        char binary_with_commas[20] = {0}; // Sufficient size for "0,0,0,0,0"
        for (int j = 0; j < 5; j++) {
            strncat(binary_with_commas, &reversed_binary[j], 1); // Append the binary digit
            if (j < 4) { // Add a comma after every digit except the last one
                strcat(binary_with_commas, ",");
            }
        }
        // Write the operation details to the CSV
        fprintf(file, "%s,%s,%d,%s,%s\n",
                op.state, 
                op.designation[0] != '\0' ? op.designation : "N/A", // Ensure designation is never empty
                op.number,
                time_str,
                binary_with_commas); // Include the comma-separated binary representation
    }
    fclose(file); // Close the file
    printf("Operations successfully written to %s\n", filename);
}
