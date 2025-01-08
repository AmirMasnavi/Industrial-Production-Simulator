#ifndef SERIAL_H

#define SERIAL_H



#include <stdio.h>



extern int serialPort;

extern char* serialString;



int openCOMPort(const char *portName, int baudRate);

void clearBuffer(int fd);

void sendData(const char *data);

void readData(char *buffer, int bufferSize);

void closeCOMPort();

void list_serial_ports();

char* showAndSelectSerialPort();

int verifyPort(const char* portName);



#endif
