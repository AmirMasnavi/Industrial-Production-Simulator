# USEI24 - Simulate Project Delays and Their Impact

## 1. Requirements Engineering

### 1.1. User Story Description
As a Product Manager, I want to simulate delays in specific activities by increasing their durations, and I want to automatically recalculate the critical path, total project duration, and slack times to assess the potential impact of these delays on the overall project schedule.

### 1.2. Customer Specifications and Clarifications 

**From the specifications document:**

>The PERT/CPM graph will store all project activities and their dependencies.

>Each activity has:
>>ID: A unique identifier for the activity.
>>Description: A brief description of the task.
>>Duration: The time required to complete the activity.
>>Dependencies: The list of predecessor activities.

>Changes in activity durations must trigger a full recalculation of:
>>Earliest and latest start/finish times.
>>Slack times.
>>Critical path(s).

**From the client clarifications:**

> **Question:** What type of information does a skill have?
>
> **Answer:** A skill only has a name, like: driver, prunner...

### 1.3. Acceptance Criteria

* **AC1:** The system must allow the user to input a delay (increased duration) for a specific activity.
* **AC2:** The system must recalculate the following after a delay is applied: The critical path(s), the total project duration, the earliest and latest start/finish times for all activities, slack times for non-critical activities.
* **AC3:** The system must notify the user of the updated schedule, including any impacted critical paths or durations.
* **AC3:** The system must validate that the new duration does not violate data integrity or dependency constraints

### 1.4. Found out Dependencies

* None

### 1.5 Input and Output Data

**Input Data:**

* Activity ID: Identifier of the activity whose duration needs to be increased.
* New Duration: The updated duration of the activity.

**Output Data:**

* Updated project schedule:
  Critical path(s).
  Total project duration.
  Earliest and latest start/finish times.
  Slack times for all activities.

### 1.6. System Sequence Diagram (SSD)

![System Sequence Diagram](svg/USEI24-system-sequence-diagram-System%20Sequence%20Diagram%20(SSD)%20-%20Simulate%20Project%20Delays%20and%20Their%20Impact.svg)

### 1.7 Other Relevant Remarks

* None