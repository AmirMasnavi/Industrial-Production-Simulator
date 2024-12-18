# Compilador
CC = gcc

# Arquivos Assembly e seus objetos 
ASMS = asm1.s asm2.s asm3.s asm4.s asm5.s asm6.s asm7.s asm8.s asm9.s asm10.s
ASM_OBJS = $(ASMS:.s=.o)

# Arquivo principal e o executável
MAIN = main.c
EXEC = prog

# Regra principal
all: $(EXEC)

# Regra para criar o executável
$(EXEC): $(ASM_OBJS) main.o
	$(CC) $(ASM_OBJS) main.o -o $(EXEC)

# Compilação dos arquivos ASM
%.o: %.s
	$(CC) -c $< -o $@

# Compilação do arquivo principal
main.o: main.c asm.h
	$(CC) -c main.c -o main.o

# Limpeza
clean:
	rm -f *.o $(EXEC)

# Depuração
debug: $(EXEC)
	gdb ./$(EXEC)

# Execução
run: $(EXEC)
	./$(EXEC)
