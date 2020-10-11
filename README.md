# Dependency Injection Simulator

## Versions

Java: Oracle Java SE 1.8

### 3rd party jars used

1. lombok - To reduce boilerplate code
2. assertj-core - For unit tests (with Junit 5)

### Assumptions

1. Only 2 scopes has been considered (SINGLETON and PROTOTYPE)
2. Only Constructor injection implemented (No setter injection)
3. Parameter injection not implemented

### Code Quality and Coverage
1. Line coverage > 98%
2. Number of Unit Test cases: 11
3. % of Pass Test cases: 100%

### Sonar Issues
No Sonar issues 

### Test cases covered
1. Given a simple object, with scope of **SINGLETON**, should create only **ONE** object in the container
2. Given a simple object, with scope of **PROTOTYPE**, should create **AS MANY AS** object in the container
3. Given a complex object (Interface and Impl which is of type **FILE**), the container should create **FILE** Implementation
4. Given a complex object (Interface and Impl which is of type **DB**), the container should create **DB** Implementation
5. Without registering the type, trying to create an object should throw *Exception*
6. Given an Interface, the container should throw *Exception*
7. Without a public constructor, trying to create an object should throw *Exception*
8. Given a simple object, the container should create the object
9. Given an Abstract class, the container should throw *Exception*
10. Given a complex object, with scope of **SINGLETON**, the container should first create the dependent object and then the actual object
11. When there exists a *cycle* in the dependency, trying to create an object should throw *Exception*

### EOF
