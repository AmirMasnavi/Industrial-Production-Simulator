# USEI22 - Identify the Critical Path

## 1. Requirements Engineering

### 1.1. User Story Descriptio
As a Product Manager, I want to identify the critical path of the project so that I can focus on activities that directly affect the project completion time

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>The critical path is the sequence of activities that determines the total project duration. Activities in the critical path have:
>>Zero slack time.
>
>>Dependencies that must be completed sequentially.

>The system must:
>>Identify the critical path(s) from the PERT/CPM graph.
>
>>Calculate the total project duration based on the critical path

**From the client clarifications:**

> **Question:** Is it the waiting time of a workstation waiting for an item to start it's operation? For example, press station is waiting for a cut operation to be done first in another station.
>
> **Answer:** No, average waiting time for items awaiting a specific operation to be carried out on them.

> **Question** When we are asked to calculate the waiting times corresponding to the average execution time of an operation, what are we referring to? The time it takes for a certain operation to be executed, with the waiting time being the sum of the execution times of previous operations, or the time it takes for the same operation to happen again?
>
> **Answer:** I'm referring to the time items wait to be processed before a type of operation. For example, if in a canteen there is a queue for soup, then another for the main course and another for dessert, I want to know how long a user waits in each queue.

> **Question:** Regarding USEI06, what's the waiting time supposed to be? Is it the waiting time of a workstation waiting for an item to start it's operation? For example, press station is waiting for a cut operation to be done first in another station.
>
> **Answer:** No, average waiting time for items awaiting a specific operation to be carried out on them.

### 1.3. Acceptance Criteria

* **AC1:** The system must identify all activities with zero slack time.
* **AC2:** The system must construct the critical path(s) based on activity dependencies and zero slack times.
* **AC3:** The system must calculate and display the total project duration based on the critical path.
* **AC4:** The system must notify the user with a clear representation of the critical path(s) and project duration.

### 1.4. Found out Dependencies

**USEI17-** The system must already support the construction of a PERT/CPM graph with activity dependencies.
**USEI20-** The system must already calculate earliest and latest start/finish times and slack times for all activities.

### 1.5 Input and Output Data

**Input Data:**

* PERT/CPM Graph containing:
  * Activities with earliest and latest start/finish times.
  * Dependencies.

**Output Data:**

* Critical path(s), including:
  * Sequence of activities in the critical path.
  * Total project duration.
* Notification to the user with a detailed critical path analysis report.

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/USEI22-system-sequence-diagram-System%20Sequence%20Diagram%20(SSD)%20-%20Identify%20the%20Critical%20Path.svg)

### 1.7 Other Relevant Remarks

* None