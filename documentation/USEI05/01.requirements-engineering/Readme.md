# USEI05 - Execution Times and Percentages by Workstation

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Manager I want to present a list of machines with total time of operationTest, and percentages relative to the operationTest time and total execution time.

### 1.2. Customer Specifications and Clarifications

**From the specifications document:**

>Each workstation performs specific operations with associated execution times.

>The total execution time for each workstation must be calculated based on all operations executed by that workstation.

>For each workstation, the system must calculate the percentage of its execution time relative to the cumulative execution time across all workstations, enabling analysis of resource usage.

>The results should be sorted in ascending order based on the percentage of execution time for each workstation to allow easier analysis of underutilized stations.

**From the client clarifications:**

> **Question:** As our group is 2 members in ESINF, we were told not to do USEI02, USEI03, USEI04, and USEI06. In USEI05, will we still have to create a simulator that processes the article to obtain the execution times? What are the criteria in this case? Thanks.
>
> **Answer:** I believe you should ask this question to the professors who defined these US for implementation. In any case, I don't understand how you can do USEI05 without doing USEI02, in fact I don't even understand what's left of the application if you don't do USEI02.

> **Question:** Can be more explicit when you refer "sorted in ascending order of the percentage of execution time relative to the total time.", the total time is refered previously as "total execution time" ?
>
> **Awnser:** The goal is to show significance of each machine labour time compared to total labour of the process!Since all machines would be shown in the list, ordering it by the percentage (significance) looks reasonable

### 1.3. Acceptance Criteria

* **AC1:** The system must accurately calculate the total execution time for each workstation.
* **AC2:** The system must calculate and display each workstation’s execution time percentage relative to the overall execution time.
* **AC3:** The results must be sorted in ascending order based on the percentage of execution time.

### 1.4. Found out Dependencies

* **USEI01-** Properly imported and structured data from workstations.csv and article.csv (USEI01).
* **USEI04-** Calculated execution times for individual operations and workstations (USEI04).

### 1.5 Input and Output Data

* Uploaded File:
    * workstations.csv
    * article.csv


**Output Data:**

* Machines use report.

### 1.6. System Sequence Diagram (SSD)
![System Sequence Diagram ](svg/USEI05-system-sequence-diagram.svg)
