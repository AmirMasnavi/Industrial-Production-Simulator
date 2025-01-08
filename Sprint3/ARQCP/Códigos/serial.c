#include <fcntl.h>

#include <termios.h>

#include <unistd.h>

#include <stdio.h>

#include <stdlib.h>

#include <string.h>

#include <dirent.h>

#include "serial.h"



int serialPort;

char* serialString;



// Function to open the COM port

int openCOMPort(const char *portName, int baudRate) {

    #ifdef NOPICO

    printf("COM port NOPICO opened successfully!\n");

    return 1;

    #endif

    // Open the COM port

    printf("Opening COM port %s...\n", portName);

    serialPort = open(portName, O_RDONLY | O_NOCTTY | O_NDELAY);

    if (serialPort == -1) {

        perror("Error: Unable to open COM port");

        return 0;

    }

    // Configure the serial port parameters

    struct termios options;

    if (tcgetattr(serialPort, &options) != 0) {

        perror("Error: Unable to get COM port attributes");

        close(serialPort);

        return 0;

    }

    // Set baud rate

    cfsetispeed(&options, baudRate);

    cfsetospeed(&options, baudRate);

    // Set data bits, stop bits, and parity

    options.c_cflag &= ~PARENB; // No parity

    options.c_cflag &= ~CSTOPB; // 1 stop bit

    options.c_cflag &= ~CSIZE;

    options.c_cflag |= CS8;     // 8 data bits

    // Enable the receiver and set local mode

    options.c_cflag |= (CLOCAL | CREAD);

    // Set timeouts (non-canonical mode)

    options.c_cc[VMIN] = 0;  // Minimum number of characters to read

    options.c_cc[VTIME] = 10; // Timeout in deciseconds (1 second)

    // Apply the settings

    if (tcsetattr(serialPort, TCSANOW, &options) != 0) {

        perror("Error: Unable to configure COM port");

        close(serialPort);

        return 0;

    }

    printf("COM port %s opened successfully!\n", portName);

    return 1;

}



void clearBuffer(int fd) {

    #ifdef NOPICO

    return;

    #endif

    // Clear both the input and output buffers

    if (tcflush(fd, TCIOFLUSH) == -1) {

        perror("Error clearing buffers");

    } else {

        printf("Buffers cleared successfully.\n");

    }

}



// Function to send data to the COM port

void sendData(const char *data) {

    #ifdef NOPICO

    printf("Sending: %s\n", data);

    return;

    #endif

    char command[512];

    printf("Sending: %s\n", data);

    snprintf(command, sizeof(command), "echo '%s' > %s", data,serialString);

    printf("Command: %s\n", command);

    int result = system(command);

    if (result) {

        perror("Error: Failed to write to COM port");

    } else {

        printf("Sent: %s\n", data);

    }



}



// Function to read data from the COM port
void readData(char *buffer, int bufferSize) {
    #ifdef NOPICO
    // Simula a leitura de dados no modo NOPICO
    const char *simulatedData = "TEMP&unit:celsius&value:20#HUM&unit:percentage&value:80";
    strncpy(buffer, simulatedData, bufferSize - 1); // Copia os dados simulados para o buffer
    buffer[bufferSize - 1] = '\0'; // Garante que a string seja terminada
    return;
    #endif

    int attempts = 5; // Número máximo de tentativas
    int bytesRead = 0;

    memset(buffer, 0, bufferSize); // Garante que o buffer esteja vazio inicialmente

    while (attempts > 0) {
        bytesRead = read(serialPort, buffer, bufferSize - 1);
        if (bytesRead > 0) {
            // Finaliza a string com '\0'
            if (bytesRead < bufferSize) {
                buffer[bytesRead] = '\0';
            } else {
                buffer[bufferSize - 1] = '\0'; // Garante que o buffer não exceda o limite
            }
            printf("Received: %s\n", buffer);
            return; // Sai da função após a leitura bem-sucedida
        } else if (bytesRead == 0) {
            printf("No data available, retrying...\n");
        } else {
            perror("Error reading from serial port");
        }

        attempts--;
        usleep(100000); // Espera 100ms antes de tentar novamente
    }

    // Se não conseguir ler nada após as tentativas, define o buffer como vazio
    buffer[0] = '\0';
    printf("Failed to read data after multiple attempts.\n");
}




// Function to close the COM port

void closeCOMPort() {

    #ifndef NOPICO

    close(serialPort);

    #endif

    printf("COM port closed.\n");

    return;

}





void list_serial_ports() {

    #ifdef NOPICO

    return;

    #endif

    struct dirent *entry;

    DIR *dp = opendir("/dev");



    if (dp == NULL) {

        perror("opendir");

        return;

    }



    while ((entry = readdir(dp))) {

        if (strncmp(entry->d_name, "ttyS", 4) == 0 || strncmp(entry->d_name, "ttyUSB", 6) == 0) {

            printf("Found serial port: /dev/%s\n", entry->d_name);

        }

    }



    closedir(dp);

}





//"/dev/ttyUSB0" FORMAT

char* showAndSelectSerialPort(){

    #ifdef NOPICO

    return "";

    #endif

   char* portName = malloc(sizeof(char)*256);// Buffer to store the selected port name

    do {

        list_serial_ports();

        // Prompt the user to enter a port name

        printf("Enter the serial port path (e.g., /dev/ttyUSB0): ");

        scanf("%255s", portName); // Safe input with buffer size limit

        printf("Selected port: %s\n", portName);

        if (verifyPort(portName) == 0) {

            serialString = portName;

            return portName; // Return the valid port name

        } else {

            printf("Invalid port. Please try again.\n");

        }



    } while (1); // Repeat until a valid port is selected

}



int verifyPort(const char* portName) {

    #ifdef NOPICO

    return 0;

    #endif

    FILE *file = fopen(portName, "r");

    if (file) {

        fclose(file);

        return 0; // Port is valid

    }

    return -1; // Port is invalid

}
