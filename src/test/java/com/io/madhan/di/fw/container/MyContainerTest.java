package com.io.madhan.di.fw.container;

import com.io.madhan.di.fw.container.sample.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.OutputStream;
import java.io.Serializable;

import static org.assertj.core.api.Assertions.assertThat;

class MyContainerTest {

    @Test
    @DisplayName("Given a simple object, the container should create the object")
    void createSimpleObject() {

        MyContainer container = MyContainer.getContainer();
        container.registerSingleton(TestObject01.class);
        TestObject01 testObject01 = container.createBean(TestObject01.class);

        assertThat(testObject01).isNotNull();
    }

    @Test
    @DisplayName("Given a simple object, with scope of Singleton, should create only ONE object in the container")
    void testSingletonScope() {

        MyContainer container = MyContainer.getContainer();
        container.registerSingleton(TestObject01.class);

        TestObject01 testObject01 = container.createBean(TestObject01.class);
        TestObject01 testObject02 = container.createBean(TestObject01.class);

        assertThat(testObject01).isSameAs(testObject02);
    }

    @Test
    @DisplayName("Given a simple object, with scope of Prototype, should create AS MANY AS object in the container")
    void testPrototypeScope() {

        MyContainer container = MyContainer.getContainer();
        container.registerPrototype(TestObject01.class);

        TestObject01 testObject01 = container.createBean(TestObject01.class);
        TestObject01 testObject02 = container.createBean(TestObject01.class);

        assertThat(testObject01).isNotSameAs(testObject02);
    }

    @Test
    @DisplayName("Given a complex object, with scope of Singleton, the container should first create the dependent object and then the actual object")
    void testDependentObjectGraph() {

        MyContainer container = MyContainer.getContainer();

        // Order of registration is NOT important
        container.registerSingleton(TestObject01.class);
        container.registerSingleton(DependencyTest01.class);

        DependencyTest01 dependencyTest01 = container.createBean(DependencyTest01.class);

        assertThat(dependencyTest01).isNotNull();
    }

    @Test
    @DisplayName("Given an Interface, the container should throw Exception")
    void testForInterface() {

        MyContainer container = MyContainer.getContainer();

        Assertions.assertThrows(IllegalArgumentException.class, () -> container.registerSingleton(Serializable.class));
    }

    @Test
    @DisplayName("Given an Interface, the container should throw Exception")
    void testForAbstractClass() {

        MyContainer container = MyContainer.getContainer();

        Assertions.assertThrows(IllegalArgumentException.class, () -> container.registerSingleton(OutputStream.class));
    }

    @Test
    @DisplayName("Given a complex object (Interface and Impl which is of type DB), the container should create DB Implementation")
    void testDependentServiceWithDB() {

        MyContainer container = MyContainer.getContainer();

        // Order of registration is NOT important
        container.registerSingleton(RepositoryService.class);
        container.registerSingleton(RepositoryI.class, RepositoryDBImpl.class); // Injecting DB implementation

        RepositoryService repoService = container.createBean(RepositoryService.class);

        assertThat(repoService).isNotNull();
        assertThat(repoService.getService().getType()).isEqualTo(RepositoryServiceType.DB);
    }

    @Test
    @DisplayName("Given a complex object (Interface and Impl which is of type FILE), the container should create FILE Implementation")
    void testDependentServiceWithFILE() {

        MyContainer container = MyContainer.getContainer();

        // Order of registration is NOT important
        container.registerSingleton(RepositoryService.class);
        container.registerSingleton(RepositoryI.class, RepositoryFileImpl.class); // Injecting File implementation

        RepositoryService repoService = container.createBean(RepositoryService.class);

        assertThat(repoService).isNotNull();
        assertThat(repoService.getService().getType()).isEqualTo(RepositoryServiceType.FILE);
    }

    @Test
    @DisplayName("Without registering the type, trying to create object should throw Exception")
    void testWithoutRegister() {

        MyContainer container = MyContainer.getContainer();

        Assertions.assertThrows(IllegalArgumentException.class, () -> container.createBean(RepositoryService.class));
    }

    @Test
    @DisplayName("Without public constructor, trying to create object should throw Exception")
    void testWithoutPublicConstructor() {

        MyContainer container = MyContainer.getContainer();
        container.registerSingleton(Math.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> container.createBean(Math.class));
    }

    @Test
    @DisplayName("When there exists a cycle in the dependency, trying to create object, should throw Exception")
    void testForCyclicDependency() {

        MyContainer container = MyContainer.getContainer();
        container.registerSingleton(CycleDependency01.class);
        container.registerSingleton(CycleDependency02.class);

        Assertions.assertThrows(IllegalArgumentException.class, () -> container.createBean(CycleDependency01.class));
    }

}
