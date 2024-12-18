.section .data
.equ SPACE, 32                # Constante para o caractere de espaço (ASCII 32)
.equ TAB, 9                   # Constante para o caractere de tabulação (ASCII 9)

.section .text
.global get_number

get_number:
    movl $0, %r10d            # Acumulador para o número final
    movl $0, %r13d            # Contador de dígitos válidos processados

loop:
    cmpb $0, (%rdi)           # Verifica se chegou ao fim da string
    je get_int_number

    # Verifica se o caractere atual é espaço ou tabulação
    cmpb $SPACE, (%rdi)       
    je continue
    cmpb $TAB, (%rdi)
    je continue

    # Verifica se o caractere atual é um dígito
    cmpb $'0', (%rdi)         
    jl fail_reset             
    cmpb $'9', (%rdi)         
    jg fail_reset             

    # Atualiza o número acumulado
    movb (%rdi), %r8b         # Carrega o caractere atual
    subb $'0', %r8b           # Converte o caractere para valor numérico
    movsbl %r8b, %r8d         # Extende o valor para 32 bits
    imull $10, %r10d          # Multiplica o acumulador por 10
    addl %r8d, %r10d          # Soma o dígito atual ao acumulador
    incq %r13                 # Incrementa o contador de dígitos válidos

    incq %rdi                 # Move para o próximo caractere
    jmp loop                  # Continua o loop

continue:
    incq %rdi                 # Ignora espaços/tabulações e vai para o próximo caractere
    jmp loop                  # Continua o loop

get_int_number:
    cmpq $0, %r13             # Verifica se processou algum dígito válido
    je fail_reset             # Se não processou, falha

    movl %r10d, (%rsi)        # Armazena o número final no ponteiro de saída
    movl $1, %eax             # Retorna sucesso (1)
    ret                       # Retorna da função

fail_reset:
    movl $0, (%rsi)           # Zera o valor no ponteiro de saída
fail:
    movl $0, %eax             # Retorna falha (0)
    ret                       # Retorna da função
    
.section .note.GNU-stack
