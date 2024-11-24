.section .text
.global dequeue_value

dequeue_value:
    # Arguments:
    # rdi -> buffer (base address)
    # rsi -> length of buffer
    # rdx -> tail pointer (address)
    # rcx -> head pointer (address)
    # r8  -> value pointer (address to store dequeued value)

    # Load *tail into %eax
    movl (%rdx), %eax         # %eax = tail

    # Compare tail with head (head vs. tail)
    cmpl %eax, (%rcx)         # Compare head == tail
    je empty_buffer                  # If head == tail, buffer is empty

    # Buffer is not empty, retrieve the value at buffer[tail]
    movl (%rdi, %rax, 4), %eax  # Load buffer[tail] into %eax
    movl %eax, (%r8)           # Store the dequeued value at *value

    # Increment the tail pointer
    incl (%rdx)                # Increment tail pointer

    # Check if tail == length
    cmpl %esi, (%rdx)          # Compare tail with length
    jne ok               # If tail != length, proceed to ok

    # If head == length, reset head to 0 (wraparound)
    movl $0, (%rdx)            # Reset head to 0

ok:
    movl $1, %eax              # Return 1 to indicate success
    ret

empty_buffer:
    movl $0, %eax              # Return 0 to indicate failure (buffer is empty)
    ret

