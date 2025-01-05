# USEI23: Identify Bottleneck Activities

## 1. Requirements Engineering

### 1.1. User Story Description

As a Product Manager, I want to identify bottleneck activities in the project, so I can focus on resolving them to improve the overall project timeline and efficiency.
 ### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>Bottleneck activities are those that:
>>Have the highest number of dependent activities.
> 
>>Appear in the most number of critical paths.
> 
>>Directly affect the project completion time.
> 
>>The system must be able to:

Analyze dependencies and critical paths in the PERT/CPM graph.
Rank activities based on their bottleneck impact.
**From the client clarifications:**

> **Question:**
>Are we supposed to present all activities and their respective "degree" of dependence / number of successors, or just present some of them, those with the highest "degree" of dependence / number of successors?
>
>





### 1.3. Acceptance Criteria

* **AC1:** The system must identify activities with the highest number of dependent activities.
* **AC2:** The system must analyze critical paths to identify activities that appear in multiple paths.
* **AC3:** The system must rank identified bottleneck activities based on their impact on the project timeline.
* **AC3:** The system must notify the user with a detailed report of identified bottlenecks and their rankings.

### 1.4. Found out Dependencies

* **USEI17-** The system must already support the construction of a PERT/CPM graph with activity dependencies.
* **USEI22-** The system must already identify critical paths to determine bottleneck activities.

### 1.5 Input and Output Data

**Input Data:**

* PERT/CPM Graph containing:
  * Activity dependencies.
  + Critical paths.

**Output Data:**

* List of bottleneck activities, including:
  * Activity ID.
  * Number of dependent activities.
  * Number of critical paths it appears in.
  * Ranking based on bottleneck impact.

* Notification to the user with a detailed bottleneck analysis report.

### 1.6. System Sequence Diagram (SSD)
![System Sequence Diagram](svg/USEI23-system-sequence-diagram-System%20Sequence%20Diagram%20(SSD)%20-%20Identify%20Bottleneck%20Activities.svg)

### 1.7 Other Relevant Remarks

* None