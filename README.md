# Summary
A sample project to demonstrate OOPS design and programming in Java for backend service. It provides simple API to calculate discount
for given user, item and related rules. The design is following SOA pattern and has 
four layers - service, business logic, data access and business entity. 

# Getting Started
Requirements:

- Apache Maven 3.3
- Java 1.7

Clone and build the project

```bash
    git clone https://github.com/ishtyaq/retailstore.git
    cd retail-store
    mvn install
```    

# Assumptions
- API supports single item bill. Multiple items need to be given one after another. 
- Multiple Discounts are applied in sequence. First is applied on full amount and subsequent on balance amount.  
- API are exclusive to JAVA, though its separated in different packages and can be exposed as rest service. 
- 48 jUNit test cases are added to verify different sceararios and  individual functions. 

# Object Oriented Approach
I have followed below approaches for developing this code.High level class diagram is also shown below. 
- Overall the requirements are centered on discount so i have put all rules in separate businesslogic class. 
- Inheritence or multiple interfaces are not used to avoid implicit complexity. 
- Each implementation function has corresonding interface to reduce coupling.  
- Same naming convention is used in each layer with differnt post-fix - BL for businesslogic, DA for data access, BE for busienss entity. 
- The core logic is added in DiscountBL class. Any changes or new rules can be added here or new class can be introduced.   


![Alt text](https://github.com/ishtyaq/retailstore/blob/master/img/class_design.png "Class Diagram")

# Test Coverage

I’ve used the [Cobertura](https://github.com/cobertura/cobertura) maven plugin to ensure the unit tests provide full coverage. The current coverage is 96% as shown in the screenshot below.

![Alt text](https://github.com/ishtyaq/retailstore/blob/master/img/code_coverage.png "Class Diagram")

To run the test coverage:

```bash
    mvn cobertura:cobertura
```
The coverage report can be accessed from target/site/cobertura/index.html directory, after installing the project.


# Leverage Today’s Best Coding Practices

[Google Checkstyle](https://github.com/checkstyle/checkstyle/blob/master/src/main/resources/google_checks.xml) is used to ensure coding styles is in standard format. 

To run the checkstyle report:

```bash
    mvn checkstyle:checkstyle
```

Code comments and data validations is added extensively. Below plug-in is also used for detecting any possible bugs in the code. 

To run FindBugs plugin:

```bash
    mvn findbugs:check
```

To run the PMD plugin:

```bash
    mvn pmd:check
```
