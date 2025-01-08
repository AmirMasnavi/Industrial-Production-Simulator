#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>
#include "machine.h"
#include "operation.h"
#include "plant_manager.h"
#include "serial.h"
#include <unistd.h>


// Function to process data from Arduino
void process_data(const char *input) {
    char temperature[16] = "";
    char humidity[16] = "";

    // Extract temperature and humidity
    const char *temp_ptr = strstr(input, "value:");
    if (temp_ptr) {
        sscanf(temp_ptr, "value:%[^#]", temperature);
    }

    const char *hum_ptr = strstr(temp_ptr ? temp_ptr + strlen("value:") : input, "value:");
    if (hum_ptr) {
        sscanf(hum_ptr, "value:%s", humidity);
    }

    // Print formatted result
    printf("data: temperature: %s, humidity: %s\n", temperature, humidity);
}

// Function to load machines from a text file
void load_machines_from_file(const char* filename, PlantFloorManager* manager) {
    FILE* file = fopen(filename, "r");
    if (!file) {
        perror("Failed to open machines.txt");
        return;
    }

    char line[256];
    while (fgets(line, sizeof(line), file)) {
        int id;
        char name[50];
        float temp_min, temp_max, hum_min, hum_max;
        int buffer_length, median_window_length;

        // Parse the line
        if (sscanf(line, "%d,%49[^,],%f,%f,%f,%f,%d,%d",
                   &id, name, &temp_min, &temp_max, &hum_min, &hum_max,
                   &buffer_length, &median_window_length) == 8) {
            // Create the machine and add it to the plant manager
            Machine* machine = setup_machine(id, name, temp_min, temp_max, hum_min, hum_max, buffer_length, median_window_length);
            if (machine) {
                add_machine(manager, machine);
            }
        } else {
            fprintf(stderr, "Invalid line format in %s: %s", filename, line);
        }
    }

    fclose(file);
}
// Function to display the menu options
void display_menu() {
    printf("\n=== Plant Floor Manager ===\n");
    printf("1. Load machines from file\n");
    printf("2. Display all machines\n");
    printf("3. Export machine operation sequence\n");
    printf("4. Display a specific machine\n");
    printf("5. Add a new machine\n");
    printf("6. Remove a machine\n");
    printf("7. Display machine state\n");
    printf("8. Display all machine statuses\n"); // Nova opção movida
    printf("9. Set machine state\n");
    printf("10. Assign operation to a machine\n");
    printf("11. Send command to Arduino\n");
    printf("12. Load instructions from file and send to Arduino\n");
    printf("0. Exit\n");
    printf("Enter your choice: ");
}


int main() {
    // Initialize the Plant Floor Manager with a capacity of 10 machines
    PlantFloorManager* manager = create_plant_manager(10);
    if (!manager) {
        fprintf(stderr, "Failed to create PlantFloorManager\n");
        return 1;
    }

    char* serialPortName = NULL;
    int serialInitialized = 0;

    int choice;
    do {
        display_menu();
        scanf("%d", &choice);

        switch (choice) {
            case 1: { // Load machines from file
                load_machines_from_file("machines.txt", manager);
                printf("Machines loaded from file successfully.\n");
                break;
            }

            case 2: { // Display all machines
                if (manager && manager->machines) {
                    printf("\n=== List of Machines ===\n");
                    for (int i = 0; i < manager->count; i++) {  // Use manager->count for the number of machines
                        Machine* machine = manager->machines[i];
                        if (machine) {
                            printf("ID: %d, Name: %s, Temp Min: %.2f, Temp Max: %.2f, Hum Min: %.2f, Hum Max: %.2f\n",
                                machine->id, machine->name, machine->temp_min, machine->temp_max,
                                machine->hum_min, machine->hum_max);
                        }
                    }
                } else {
                    printf("No machines available.\n");
                }
                break;
            }
case 3: { // Export machine operation sequence
    int id;
    char filename[50];
    printf("Enter machine ID: ");
    scanf("%d", &id);

    // Retrieve the machine by ID
    Machine* machine = get_machine_by_id(manager, id);
    if (!machine) {
        printf("Error: Machine with ID %d not found.\n", id);
        break;
    }

    // Check if the machine has operations
    if (machine->operation_count == 0) {
        printf("No operations recorded for Machine %d.\n", id);
        break;
    }

    // Ask user for the filename to export the operations to
    printf("Enter filename (e.g., operations.csv): ");
    scanf("%s", filename);

    // Export the operations to the CSV file
    write_machine_operations_to_csv(filename, machine->operations, machine->operation_count);
    break;
}
 case 4: { // Display a specific machine
                if (manager && manager->machines) {
                    int id;
                    printf("Enter the ID of the machine you want to view: ");
                    scanf("%d", &id);

                    Machine* machine = get_machine_by_id(manager, id);
                    if (machine) {
                        printf("\n=== Machine Details ===\n");
                        printf("ID: %d\n", machine->id);
                        printf("Name: %s\n", machine->name);
                        printf("Temp Min: %.2f\n", machine->temp_min);
                        printf("Temp Max: %.2f\n", machine->temp_max);
                        printf("Hum Min: %.2f\n", machine->hum_min);
                        printf("Hum Max: %.2f\n", machine->hum_max);

                        char* state = get_machine_state(manager, id);
                        if (state) {
                            printf("Current State: %s\n", state);
                        } else {
                            printf("Current State: Unknown\n");
                        }
                    } else {
                        printf("No machine found with ID %d.\n", id);
                    }
                } else {
                    printf("No machines available.\n");
                }
                break;
            }
case 5: { // Add a new machine
    int id;
    char name[50];
    float temp_min, temp_max, hum_min, hum_max;
    int buffer_length, median_window_length;

    // Input ID
    printf("Enter machine ID: ");
    scanf("%d", &id);

    // Check if machine with this ID already exists
    if (get_machine_by_id(manager, id)) {
        printf("Error: Machine with ID %d already exists.\n", id);
        break;
    }

    // Input machine name
    printf("Enter machine name: ");
    scanf("%s", name);  // Read the machine name

    // Input the temperature and humidity ranges
    printf("Enter minimum temperature: ");
    scanf("%f", &temp_min);

    printf("Enter maximum temperature: ");
    scanf("%f", &temp_max);

    printf("Enter minimum humidity: ");
    scanf("%f", &hum_min);

    printf("Enter maximum humidity: ");
    scanf("%f", &hum_max);

    // Input buffer length and median window length
    printf("Enter buffer length: ");
    scanf("%d", &buffer_length);

    printf("Enter median window length: ");
    scanf("%d", &median_window_length);

    // Create a new machine with the provided details
    Machine *newMachine = setup_machine(id, name, temp_min, temp_max, hum_min, hum_max, buffer_length, median_window_length);
    if (newMachine) {
        // Add the new machine to the manager
        add_machine(manager, newMachine);
        printf("Machine '%s' with ID %d added successfully.\n", name, id);
    } else {
        printf("Failed to add machine.\n");
    }
    break;
}
            case 6: { // Remove a machine
                int id;
                printf("Enter machine ID to remove: ");
                scanf("%d", &id);

                if (remove_machine(manager, id) == 0) {
                    printf("Machine %d removed successfully.\n", id);
                }
                break;
            }

            case 7: { // Display machine state
                int id;
                printf("Enter machine ID: ");
                scanf("%d", &id);

                char* state = get_machine_state(manager, id);
                if (state) {
                    printf("Machine %d state: %s\n", id, state);
                }
                break;
            }
case 8: { // Display all machine statuses
    if (manager && manager->machines) {
        printf("\n=== All Machine Statuses ===\n");
        for (int i = 0; i < manager->count; i++) { // Itera sobre todas as máquinas no manager
            Machine* machine = manager->machines[i];
            if (machine) {
                char* state = get_machine_state(manager, machine->id);
                if (state) {
                    printf("ID: %d, Name: %s, Status: %s\n",
                        machine->id,
                        machine->name,
                        state);
                } else {
                    printf("ID: %d, Name: %s, Status: Unknown\n",
                        machine->id,
                        machine->name);
                }
            }
        }
        printf("============================\n");
    } else {
        printf("No machines available.\n");
    }
    break;
}



case 9: { // Set machine state
    int id;
    char state[10];
    printf("Enter machine ID: ");
    scanf("%d", &id);
    // Check if machine with this ID already exists
    if (!get_machine_by_id(manager, id)) {
        printf("Error: Machine with ID %d does not exists.\n", id);
        break;
    }

    // Ask for the new state and check if it's valid
    printf("Enter new state (ON/OFF/OP): ");
    scanf("%s", state);

    // Validate the state input
    if (strcmp(state, "ON") != 0 && strcmp(state, "OFF") != 0 && strcmp(state, "OP") != 0) {
        printf("Error: Invalid state '%s'. Please enter one of the following states: ON, OFF, OP.\n", state);
        break;
    }

    // Call the set_machine_state function
    if (set_machine_state(manager, id, state) == 0) {
        printf("State of Machine %d updated to %s.\n", id, state);
    } else {
        printf("Error: Could not update state of Machine %d.\n", id);
    }
    break;
}
case 10: { // Assign operation to a machine
    int id, operation_number;
    char operation_name[50];
    printf("Enter machine ID: ");
    scanf("%d", &id);

    // Obter a máquina pelo ID
    Machine* selectedMachine = get_machine_by_id(manager, id);
    if (!selectedMachine) {
        printf("Machine with ID %d not found.\n", id);
        break;
    }

    printf("Enter operation name: ");
    scanf("%s", operation_name);
    printf("Enter operation number (0-31): ");
    scanf("%d", &operation_number);

    // Atribuir a operação à máquina
    if (assign_operation_to_machine(manager, id, operation_name, operation_number) == 0) {
        printf("Operation assigned successfully.\n");

        // Atualizar o estado da máquina para OP
        if (set_machine_state(manager, id, "OP") == 0) {
            printf("Machine %d state updated to OP.\n", id);
        } else {
            printf("Failed to update machine %d state to OP.\n", id);
        }
    } else {
        printf("Failed to assign operation to machine %d.\n", id);
    }

    break;
}

case 11: { // Enviar comando e visualizar os dados recebidos
    static int serialInitialized = 0;
    static char machineID[50] = ""; // Variável para armazenar o ID da máquina
    char command[128];
    char buffer[256];

    // Perguntar o ID da máquina
    printf("Enter the machine ID: ");
    scanf("%s", machineID); // Armazena o ID da máquina

    // Obter a máquina pelo ID
    Machine* selectedMachine = get_machine_by_id(manager, atoi(machineID));
    if (!selectedMachine) {
        printf("Machine with ID %s not found.\n", machineID);
        break;
    }

    // Inicializar a porta serial
    if (!serialInitialized) {
        printf("Select the serial port:\n");
        serialPortName = showAndSelectSerialPort();
        if (!openCOMPort(serialPortName, 9600)) {
            printf("Failed to initialize serial port.\n");
            break;
        }
        serialInitialized = 1;
    }

    // Enviar comando para o Arduino
    printf("Enter command to send to Arduino (e.g., on,1,1,1,1,1): ");
    scanf(" %[^\n]", command); // Lê o comando do usuário no formato especificado
    sendData(command);
    printf("Command sent successfully. Machine ID: %s\n", machineID);

    // Atualizar o estado da máquina com a função existente
    char commandType[10]; // Variável para armazenar a primeira parte do comando
    strncpy(commandType, command, sizeof(commandType) - 1); // Copia o comando para análise
    commandType[sizeof(commandType) - 1] = '\0'; // Garante o término da string

    char* token = strtok(commandType, ","); // Extrai a parte antes da primeira vírgula

    // Determinar o novo estado e atualizar a máquina
    if (strcmp(token, "on") == 0) {
        if (set_machine_state(manager, atoi(machineID), "ON") != 0) {
            printf("Failed to update machine %s state to ON. Aborting operation.\n", machineID);
            break;
        }
    } else if (strcmp(token, "off") == 0) {
        if (set_machine_state(manager, atoi(machineID), "OFF") != 0) {
            printf("Failed to update machine %s state to OFF. Aborting operation.\n", machineID);
            break;
        }
    } else if (strcmp(token, "op") == 0) {
        if (set_machine_state(manager, atoi(machineID), "OP") != 0) {
            printf("Failed to update machine %s state to OP. Aborting operation.\n", machineID);
            break;
        }
    }

    // Exibir o estado atualizado da máquina para debug
    printf("DEBUG: Machine %s current state: %s\n", machineID,
           (selectedMachine->state == ON) ? "ON" :
           (selectedMachine->state == OFF) ? "OFF" : "OP");

    // Perguntar se o usuário deseja visualizar os dados recebidos
    char option;
    printf("Do you want to read the data received? (y/n): ");
    scanf(" %c", &option);

    if (option == 'y' || option == 'Y') {
        printf("Reading data from Arduino...\n");
        readData(buffer, sizeof(buffer));

        // Remove espaços extras e caracteres não visíveis, se necessário
        buffer[strcspn(buffer, "\r\n")] = '\0'; // Remove \r ou \n no final

        // Processar os dados recebidos (temperatura e umidade)
        float receivedTemp = 0.0;
        float receivedHum = 0.0;

        sscanf(buffer, "TEMP&unit:celsius&value:%f#HUM&unit:percentage&value:%f", &receivedTemp, &receivedHum);
        printf("Processed data: Temperature = %.2f°C, Humidity = %.2f%%\n", receivedTemp, receivedHum);

        // Perguntar ao usuário se deseja verificar os alertas
        printf("Do you want to check for alerts based on the received data? (y/n): ");
        char alertOption;
        scanf(" %c", &alertOption);

        if (alertOption == 'y' || alertOption == 'Y') {
            // Comparar os valores recebidos com os limites da máquina
            checkAlerts(selectedMachine, receivedTemp, receivedHum);
        }
    }

    // Resetar variáveis para permitir nova interação
    serialInitialized = 0;
    memset(machineID, 0, sizeof(machineID)); // Limpar o conteúdo da variável machineID
    closeCOMPort(); // Fechar a porta serial para reiniciar na próxima interação
    printf("Case 11 completed. Ready for new interaction.\n");

    break;
}


case 12: { // Load instructions from file and send to Arduino
    if (!serialInitialized) {
        printf("Select the serial port:\n");
        serialPortName = showAndSelectSerialPort();
        if (!openCOMPort(serialPortName, 9600)) {
            printf("Failed to initialize serial port.\n");
            break;
        }
        serialInitialized = 1;
    }

    char filename[128];
    printf("Enter the filename containing instructions: ");
    scanf("%s", filename);

    FILE *file = fopen(filename, "r");
    if (!file) {
        perror("Failed to open instructions file");
        break;
    }

    char instruction[128];
    while (fgets(instruction, sizeof(instruction), file)) {
        // Remove trailing newline character
        instruction[strcspn(instruction, "\r\n")] = '\0';

        // Process the instruction to extract the ID and command
        int id;
        char state[10];
        char command[128];

        // Parse the instruction in the format: ID,STATE,COMMAND
        if (sscanf(instruction, "%d,%9[^,],%127[^\n]", &id, state, command) != 3) {
            printf("Invalid instruction format: %s\n", instruction);
            continue;
        }

        // Obter a máquina pelo ID
        Machine* machine = get_machine_by_id(manager, id);
        if (!machine) {
            printf("Machine with ID %d not found. Skipping instruction.\n", id);
            continue;
        }

        // Verificar se a máquina já está ocupada para transições inválidas
        if (machine->state == OP && strcmp(state, "ON") != 0) {
            printf("Error: Machine %d is already performing an operation. Ignoring instruction: %s\n", id, instruction);
            continue; // Ignorar esta instrução
        }

        // Atualizar o estado da máquina
        if (set_machine_state(manager, id, state) == 0) {
            printf("Machine %d state updated to %s.\n", id, state);
        } else {
            printf("Failed to update machine %d state to %s.\n", id, state);
            continue; // Ignorar envio do comando se o estado não foi atualizado
        }

        // Enviar o comando ao Arduino
        printf("Sending instruction: %s\n", command);
        sendData(command);

        // Delay entre comandos
        usleep(500000); // 500ms de delay para estabilidade
    }

    fclose(file);
    printf("Instructions sent successfully and machine states updated.\n");
    break;
}


            case 0: // Exit
                printf("Exiting program.\n");
                break;

            default:
                printf("Invalid choice. Please try again.\n");
        }
    } while (choice != 0);

 // Free resources
    free_plant_manager(manager);
    if (serialInitialized) {
        closeCOMPort();
        free(serialPortName);
    }

    return 0;
}