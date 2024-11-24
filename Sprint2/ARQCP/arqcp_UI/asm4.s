.section .text
.global format_command

format_command:

    # Save the current state of the program
    pushq %rbp                    # Save the previous base pointer
    movq %rsp, %rbp               # Set the new base pointer

    # Check if the value of n (stored in %rsi) is within the range [0, 31]
    cmpq $0, %rsi                 # Compare n with 0
    jl invalid_value             # If n < 0, jump to handle invalid value
    cmpq $31, %rsi                # Compare n with 31
    jg invalid_value             # If n > 31, jump to handle invalid value

    # Ignore leading spaces or tabs in the input string (command in %rdi)
    
check_if_space:
    cmpb $' ', (%rdi)             # Compare the current character with a space
    je next_position              # If it's a space, jump to .skip_start
    jmp check_lowercase           # Otherwise, proceed to check the command

next_position:
    incq %rdi                     # Advance the string pointer
    jmp check_if_space         	  # Continue skipping leading spaces or tabs

# Check if the command starts with 'o' or 'O' (case-insensitive)
check_lowercase:
    cmpb $'o', (%rdi)             # Compare the character with 'o'
    jne check_uppercase           # If not 'o', check for 'O'
    jmp next_verification            # If 'o', proceed to determine the operation

check_uppercase:
    cmpb $'O', (%rdi)             # Compare the character with 'O'
    jne invalid               # If not 'O', it's an invalid command
    jmp next_verification            # If 'O', proceed to determine the operation

# Determine the specific operation ('ON', 'OP', or 'OFF')
next_verification:
    incq %rdi                     # Move to the next character in the command
    
    cmpb $'N', (%rdi)             # Check if it's 'N' (for "ON")
    je check_on                # If 'N', match "ON"
    
    cmpb $'n', (%rdi)             # Check if it's 'n' (for "ON")
    je check_on                  # If 'n', match "ON"
    
    cmpb $'P', (%rdi)             # Check if it's 'P' (for "OP")
    je check_op                 # If 'P', match "OP"
    
    cmpb $'p', (%rdi)             # Check if it's 'p' (for "OP")
    je check_op                  # If 'p', match "OP"
    
    cmpb $'F', (%rdi)             # Check if it's 'F' (for "OFF")
    je check_off                  # If 'F', continue checking "OFF"
    
    cmpb $'f', (%rdi)             # Check if it's 'f' (for "OFF")
    je check_off                  # If 'f', continue checking "OFF"
    
    jmp invalid              # If no match, it's an invalid command

# Handle invalid commands
invalid:
    xorq %rax, %rax               # Indicate failure by setting the return value to 0
    call clear_buffer             # Clear the output buffer
    jmp end                       # Exit the function

# Match the "ON" command
check_on:
    incq %rdi                     # Advance to the next character
    call check_end                # Ensure the rest of the string is valid
    jnz invalid               # If not valid, handle as an invalid command

    movb $'O', (%rdx)             # Write "ON" to the output buffer
    incq %rdx
    movb $'N', (%rdx)
    incq %rdx
    jmp validate_value            # Proceed to validate the numeric value

# Check if the "OFF" command is valid
check_off:
    incq %rdi                     # Move to the next character
    cmpb $'F', (%rdi)             # Check for the second 'F' in "OFF"
    je match_off                  # If it matches, proceed
    cmpb $'f', (%rdi)             # Check for lowercase 'f'
    je match_off                  # If it matches, proceed
    jmp invalid               # Otherwise, it's an invalid command

# Match the "OP" command
check_op:
    incq %rdi                     # Advance to the next character
    call check_end                # Ensure the rest of the string is valid
    jnz invalid               # If not valid, handle as an invalid command

    movb $'O', (%rdx)             # Write "OP" to the output buffer
    incq %rdx
    movb $'P', (%rdx)
    incq %rdx
    jmp validate_value            # Proceed to validate the numeric value

# Match the "OFF" command
match_off:
    incq %rdi                     # Advance to the next character
    call check_end                # Ensure the rest of the string is valid
    jnz invalid              # If not valid, handle as an invalid command

    movb $'O', (%rdx)             # Write "OFF" to the output buffer
    incq %rdx
    movb $'F', (%rdx)
    incq %rdx
    movb $'F', (%rdx)
    incq %rdx
    jmp validate_value            # Proceed to validate the numeric value

# Ensure the rest of the string only contains trailing whitespace or is empty
check_end:
check_if_space2:
    cmpb $' ', (%rdi)             # Check for a space
    je skip_next                 # Skip it if found
    jmp end_check                # Otherwise, check for end of string

skip_next:
    incq %rdi                     # Move to the next character
    jmp check_if_space2        # Continue skipping whitespace

end_check:
    cmpb $0, (%rdi)               # Check for null terminator (end of string)
    sete %al                      # Set %al to 1 if at the end of the string
    movzbl %al, %eax              # Zero-extend %al to %eax
    ret                           # Return with the result in %eax

# Validate and process the numeric value n
validate_value:
    jmp process_num               # If valid, process the numeric value

invalid_value:
    xorq %rax, %rax               # Set return value to 0 (failure)
    call clear_buffer             # Clear the output buffer
    jmp end                       # Exit the function

# Process the numeric value n and convert it to binary representation
process_num:
    movl $16, %ecx                # Set a mask for 5 bits (0x10)

binary_loop:
    movb $',', (%rdx)             # Write a comma to the buffer
    incq %rdx                     # Advance the buffer pointer
    testl %ecx, %esi              # Test the current bit of n
    jnz set_one                   # If the bit is 1, write '1'
    movb $'0', (%rdx)             # If the bit is 0, write '0'
    jmp next_digit

set_one:
    movb $'1', (%rdx)             # Write '1' to the buffer

next_digit:
    incq %rdx                     # Advance the buffer pointer
    shrl $1, %ecx                 # Shift the mask to the next bit
    cmp $0, %ecx                  # Check if all bits are processed
    jnz binary_loop               # If not, continue processing

    movb $0, (%rdx)               # Null-terminate the buffer
    movl $1, %eax                 # Set return value to 1 (success)
    jmp end                       # Exit the function

# Clear the output buffer by filling it with zeros
clear_buffer:
    movq $20, %rcx                # Set the buffer size (20 bytes)
zero_fill_loop:
    movb $0, (%rdx)               # Write zero to the buffer
    incq %rdx                     # Advance the buffer pointer
    loop zero_fill_loop           # Repeat until the buffer is cleared
    ret                           # Return

end:
    popq %rbp                     # Restore the previous base pointer
    ret                           # Return to the caller


.section .note.GNU-stack
