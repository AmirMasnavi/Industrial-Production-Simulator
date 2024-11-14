# USEI02 - Implementing a Simulator

## 1. Requirements Engineering

### 1.1. User Story Description

Aa Product Manager I want to implement a simulator that processes all the article.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

> The simulator should load data from two input files: article.csv and workstations.csv.

> The system should also handle article priority when assigning article to workstations.

> The simulator must create a queue for each operationTest needed by the article and assign them to workstations based on availability and processing speed.


**From the client clarifications:**

> **Question:** Can an article have the same operationTest applied to it more than once? Or can we assume that any operationTest only shows once in the list?
>
> **Answer:** You can asume, at least for now, that an operationTest applies to an article just once.

> **Question:** There are repeated lines in the article.csv, should we consider each one has a production order?
>
> **Answer:** Each line corresponds to one article to be produced.

### 1.3. Acceptance Criteria

* **AC1:** The simulator must create a queue for each operationTest, containing all article whose next operationTest is that of the specified queue.
* **AC2:** The simulator must assign article in the queue to the available machine that can perform the required operationTest the fastest, in the order of their entry into the queue.
* **AC3:** The system should calculate the total production time for the article, taking into account the time spent in the queue and the processing time at the workstation.
* **AC4:** The results should display a list of workstations with the total operationTest time and the percentage of operationTest time relative to total execution time, in ascending order.

### 1.4. Found out Dependencies

* **USEI01** - provides the necessary data structures to store the article and workstation information essential for processing simulation.


### 1.5 Input and Output Data

**Input Data:**

* Uploaded File:
  * workstations.csv
  * article.csv


**Output Data:**

* A list of the total time each workstation spent processing article, 
* A list of the percentage of time each workstation was active relative to the total simulation time
* A list of the total production time for all the article processed and the sequence of operations each article went through and the time spent in queues

### 1.6. System Sequence Diagram (SSD)


![System Sequence Diagram ](svg/USEI02-system-sequence-diagram.svg)

### 1.7 Other Relevant Remarks

* None