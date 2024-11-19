# USEI04 - Calculation of Execution Times by Each Operation

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Manager, I want to calculate the execution times taken by each operation.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>Each item undergoes a series of operations (e.g., cutting, drilling) with specific execution times, performed at designated workstations.

>The system must be capable of identifying and summing up the execution times for each unique operation across all items in the system.

>Execution time per operation should be provided in a user-friendly format, with each unique operation type listed alongside its cumulative time.

**From the client clarifications:**

> **Question:** The USEI04 states that we should calculate execution times by each operation. Does it mean that we should calculate the total time needed for each operation to be concluded? So if 2 operations of the same type like "cutting" was concluded it should be both of the times added to each other
>
> **Answer:** It means labour time for an operation type.


### 1.3. Acceptance Criteria

* **AC1**: The system must correctly calculate the cumulative execution time for each unique operation across all items.
* **AC2:** The program must return the total execution time taken by EACH operation.


### 1.4. Found out Dependencies

* **USEI01**-Successful import and structure of data from articles.csv and workstations.csv .
* **USEI03**-Calculation of total production time for items, as each operation’s time is essential for summing total execution times.

### 1.5 Input and Output Data

**Input Data:**

* Uploaded File:
  * workstations.csv
  * articles.csv



**Output Data:**

* Total execution time of each operation.

### 1.6. System Sequence Diagram (SSD)
![System Sequence Diagram ](svg/USEI04-system-sequence-diagram.svg)

### 1.7 Other Relevant Remarks

* None