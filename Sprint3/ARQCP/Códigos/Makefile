CC = gcc
CFLAGS = -Wall -Wextra -g
OBJ = main.o machine.o plant_manager.o operation.o serial.o binaryNumber.o 

all: main

main: $(OBJ)
	$(CC) $(CFLAGS) -o main $(OBJ)

main.o: main.c machine.h plant_manager.h
	$(CC) $(CFLAGS) -c main.c

machine.o: machine.c machine.h operation.h
	$(CC) $(CFLAGS) -c machine.c

plant_manager.o: plant_manager.c plant_manager.h machine.h operation.h
	$(CC) $(CFLAGS) -c plant_manager.c

operation.o: operation.c operation.h binaryNumber.h
	$(CC) $(CFLAGS) -c operation.c

binaryNumber.o: binaryNumber.s
	$(CC) -c binaryNumber.s -o binaryNumber.o

clean:
	rm -f *.o main

