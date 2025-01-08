.global get_number_binary
get_number_binary:
    cmpl $0, %edi                         # Check if input is negative
    jl error
    cmpl $31, %edi                        # Check if input is greater than 31
    jg error

    movl $5, %r8d                         # Counter for 5 bits
    movl %edi, %eax                       # Load input number into %eax

loop:
    cmpl $0, %r8d                         # Check if counter is zero
    je end

    movl $2, %r9d                         # Load divisor (2)
    cdq                                   # Sign-extend %eax to %edx:%eax
    idivl %r9d                            # Divide %eax by 2, quotient in %eax, remainder in %edx
    addb $48, %dl                         # Convert remainder to ASCII ('0' or '1')
    movb %dl, (%rsi)                      # Store ASCII character in memory

    subl $1, %r8d                         # Decrement counter
    addq $1, %rsi                         # Increment memory pointer
    jmp loop

end:
    movb $0, (%rsi)                       # Null-terminate output
    movl $1, %eax                         # Success indicator
    ret

error:
    movb $0, (%rsi)                       # Write 0 on error
    movl $0, %eax                         # Return 0 to indicate an error
    ret
