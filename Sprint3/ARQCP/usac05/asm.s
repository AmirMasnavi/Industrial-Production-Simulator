.section .text
    .global enqueue_value

enqueue_value:
    movq %rdi, %rax         # Load buffer base address into %rax
    movl %esi, %r9d         # Load length of buffer into %r9d
    movl (%rdx), %r10d      # Load current tail position into %r10d
    movl (%rcx), %r11d      # Load current head position into %r11d

    # Insert the value at buffer[tail]
    movl %r8d, (%rax, %r10, 4)

    # Increment tail position
    addl $1, %r10d
    cmpl %r9d, %r10d        # Check if tail reaches the buffer length
    jl update_tail          # If not, continue
    movl $0, %r10d          # Wrap tail to 0 if it reached the buffer length

update_tail:
    # Check if the buffer is full
    cmpl %r10d, %r11d       # Compare updated tail with head
    jne not_full            # If not equal, buffer is not full

    # If buffer is full, increment head to make room
    addl $1, %r11d
    cmpl %r9d, %r11d        # Check if head reaches the buffer length
    jl update_head          # If not, continue
    movl $0, %r11d          # Wrap head to 0 if it reached the buffer length

update_head:
    movl %r10d, (%rdx)      # Update tail position in memory
    movl %r11d, (%rcx)      # Update head position in memory
    movl $1, %eax           # Return 1 (buffer is full)
    ret

not_full:
    movl %r10d, (%rdx)      # Update tail position in memory
    movl %r11d, (%rcx)      # Keep head position unchanged
    movl $0, %eax           # Return 0 (buffer is not full)
    ret

.section .note.GNU-stack
