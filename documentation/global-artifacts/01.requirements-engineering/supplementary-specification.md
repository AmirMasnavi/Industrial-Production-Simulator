# Supplementary Specification (FURPS+)

## Functionality

_Specifies functionalities that:  
&nbsp; &nbsp; (i) are common across several US/UC;  
&nbsp; &nbsp; (ii) are not related to US/UC, namely: Audit, Reporting and Security._


* **Product Management**: Manage product and product variants (BOM and BOO structures).

* **Production Planning**: Create and manage production plans based on customer orders and machine availability.
* **Order Management**: Support multiple customer orders, including handling product variants (size, color).
* **Simulation Tool**: Simulate production to identify machine utilization, bottlenecks, and waiting times.
* **User Profiles & Access**: Support roles such as Production Manager, Plant Floor Manager, and Administrator.
* **Data Import**: Integrate legacy data and manage data input through PL/SQL.
* **Equipment Management**: Specify production lines and machines, linking them with operations.

_Business Rules:_

* Customer orders must include delivery dates, product variants, and customer details (e.g., NIF).
* Each article in a customer order must generate unique production orders.
* BOM and BOO must be respected for production sequences and material requirements.



## Usability

_The system should be easy for managers and operators to use, with interfaces that support efficient workflows._

* **Intuitive User Interface**: Text-based UI for the simulator; graphical visualization of product structures.
* **Accessibility**: Ensure easy data retrieval and clear display of simulation results for decision-making.
* **Documentation**: Javadoc to support developers and documentation for users.
* **Consistency**: Use consistent design patterns and follow coding standards (CamelCase).





## Reliability

_The system should function reliably, given that it deals with real-time production planning and execution._

* **Data Integrity**: Ensure accurate data input/output with PL/SQL-managed constraints in the database.
* **Availability**: Ensure the system has high availability (e.g., 99.9% uptime) to minimize production delays.
* **Error Recovery**: Automatic recovery from system errors, especially during data imports or machine simulations.

## Performance

_Performance is crucial for production operations and real-time simulations._

* **Response Time**: Operations like retrieving production orders or simulation results should respond within 1 second.
* **Throughput**: The system should handle several simultaneous customer orders without performance degradation.
* **Scalability**: Should accommodate an increasing number of machines, orders, and users without re-architecture.
* **Efficient Simulation**: The simulation tool must quickly process large data sets of operations and machines.


## Supportability

_The system should be easy to maintain and extend with future functionalities._

* **Modular Design**: Use OOP principles for easier maintenance and extensibility.
* **Documentation**: Ensure all code is well-documented using Javadoc; maintain design diagrams in SVG.
* **Multi-language Integration**: Java for core components, PL/SQL for database management, and C/Assembly for machine interaction.
* **Monitoring Tools**: Provide tools to track machine utilization and production flow bottlenecks.
* **Testability**: Follow Test-Driven Development (TDD) for all modules to ensure high-quality code.


[//]: # (## +)

[//]: # ()
[//]: # (### Design Constraints)

[//]: # ()
[//]: # (_Specifies or constraints the system design process. Examples may include: programming languages, software process, mandatory standards/patterns, use of development tools, class library, etc._)

[//]: # ()
[//]: # (* The application must be developed in Java )

[//]: # (* The application will be developed using the IntelliJ IDE and graphical interface from JavaFX)

[//]: # (* The unit tests should be implemented using  JUnit 5.)

[//]: # (* The JaCoCo plugin will generate the coverage report.)

[//]: # ()
[//]: # (### Implementation Constraints)

[//]: # ()
[//]: # (_Specifies or constraints the code or construction of a system such)

[//]: # (as: mandatory standards/patterns, implementation languages,)

[//]: # (database integrity, resource limits, operating system._)

[//]: # ()
[//]: # (*  The graphical interface needs to be developed in JavaFX.)

[//]: # (*  The application will be developed in Java language using the IntelliJ IDE.)

[//]: # (*  The app must support English language.)

[//]: # (*  The development team must implement unit tests for all methods, except for methods that implement Input/Output operations.)

[//]: # (*  The unit tests should be implemented using the JUnit 5 framework.)

[//]: # (*  The JaCoCo plugin will generate the coverage report.)

[//]: # (*  The team must adopt recognized coding standards &#40;e.g., CamelCase&#41;;)

[//]: # ()
[//]: # ()
[//]: # ()
[//]: # (### Interface Constraints)

[//]: # ()
[//]: # (* The graphical interface needs to be developed in JavaFX.)

[//]: # ()
[//]: # (### Physical Constraints)

[//]: # ()
[//]: # (* n/a)