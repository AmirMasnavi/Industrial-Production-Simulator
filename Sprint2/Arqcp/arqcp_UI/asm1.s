.section .text
.global extract_data

extract_data:

    # Save the current base pointer and set the new stack frame
    movq %rbp, %r15       # Save the original base pointer in register r15
    movq %rsp, %rbp       # Set the base pointer to the current stack pointer

    # Initialize outputs to default values
    movq $0, (%rdx)       # Set the first element of the unit array to null (empty string)
    movq $0, (%rcx)       # Initialize value to 0

    # Read the first character of the string str
    movb (%rsi), %r10b    # Load the first byte of str into r10b
    testb %r10b, %r10b    # Test if it is null (end of string)
    jz handle_null        # If null, jump to handle_null

process_loop:
    # Check the token against the current string position
    movb (%rdi), %r9b     # Load the current character of str into r9b
    testb %r9b, %r9b      # Test if it is null (end of string)
    jz handle_null        # If null, jump to handle_null
    movq %rsi, %r10       # Copy rsi (token pointer) into r10
    movq %rdi, %rbx       # Copy rdi (current str pointer) into rbx

compare_chars:
    # Compare token with the current substring of str
    movb (%r10), %r11b    # Load a character from token into r11b
    test %r11b, %r11b     # Test if it is null (end of token)
    jz check_char         # If null, jump to check_char
    movb (%rbx), %r12b    # Load the corresponding character from str into r12b
    cmpb %r12b, %r11b     # Compare the characters
    jne skip_char         # If not equal, jump to skip_char
    incq %rbx             # Increment str pointer
    incq %r10             # Increment token pointer
    jmp compare_chars     # Continue comparing characters

check_char:
    # Check if the token match is followed by '&'
    movb (%rbx), %r13b    # Load the next character from str
    cmpb $'&', %r13b      # Compare it to '&'
    jne skip_char         # If not '&', jump to skip_char
    jmp match_found       # Otherwise, jump to match_found

skip_char:
    # Move to the next character in str and continue searching
    incq %rdi             # Increment the str pointer
    jmp process_loop      # Continue the loop

match_found:
    # Skip the "&unit:" prefix
    addq $6, %rbx         # Move str pointer past "&unit:"

process_unit:
    # Extract the unit string
    movb (%rbx), %r12b    # Load the current character of the unit
    test %r12b, %r12b     # Test if it is null
    jz handle_null        # If null, jump to handle_null
    cmpb $'&', %r12b      # Check for the '&' delimiter
    je locate_value       # If found, jump to locate_value
    movb %r12b, (%rdx)    # Copy the character to the unit output
    incq %rbx             # Increment the str pointer
    incq %rdx             # Increment the unit output pointer
    jmp process_unit      # Continue extracting the unit string

locate_value:
    # Initialize value to 0 and skip "&value:"
    movq $0, %rax         # Clear rax (value accumulator)
    movb $0, (%rdx)       # Null-terminate the unit string
    addq $7, %rbx         # Move str pointer past "&value:"
    movq $0, %r12         # Clear r12 (temporary for digits)

store_value:
    # Extract and compute the numeric value
    movb (%rbx), %r12b    # Load the current character
    cmpb $'#', %r12b      # Check for the '#' delimiter
    je finalize           # If found, jump to finalize
    cmpb $0, %r12b        # Check for null (end of string)
    jz finalize           # If null, jump to finalize
    cmpb $'9', %r12b      # Ensure the character is <= '9'
    ja handle_null        # If not, jump to handle_null
    cmpb $'0', %r12b      # Ensure the character is >= '0'
    jb handle_null        # If not, jump to handle_null
    subb $'0', %r12b      # Convert ASCII digit to numeric value
    imulq $10, %rax       # Multiply current value by 10
    addq %r12, %rax       # Add the new digit to the value
    incq %rbx             # Increment the str pointer
    jmp store_value       # Continue extracting the numeric value

finalize:
    # Finalize the output and return success
    movq %rax, (%rcx)     # Store the extracted value in the output
    movq $1, %rax         # Set return value to 1 (success)
    jmp complete          # Jump to complete

handle_null:
    # Handle failure case
    movq $0, %rax         # Set return value to 0 (failure)
    jmp complete          # Jump to complete

complete:
    # Restore the original base pointer and return
    movq %r15, %rbp       # Restore the saved base pointer
    ret                   # Return to caller
    
.section .note.GNU-stack
