# Corrected Makefile for compiling asm.s and main.c with debug information

CC = gcc
CFLAGS = -g -Wall -Wextra -fanalyzer
LDFLAGS = -z noexecstack
OBJ = main.o asm.o

all: main

# Rule to create the main executable
main: $(OBJ)
	$(CC) $(OBJ) $(LDFLAGS) -o main

# Compile main.c to object file with debug info
main.o: main.c asm.h
	$(CC) $(CFLAGS) -c main.c -o main.o

# Assemble asm.s to object file with debug info
asm.o: asm.s
	$(CC) $(CFLAGS) -c asm.s -o asm.o

# Run the program
run: main
	./main

# Clean up generated files
clean:
	rm -f *.o main

