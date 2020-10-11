package com.io.madhan.di.fw.container;

import lombok.SneakyThrows;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

// IoC Container
// Singleton
public final class MyContainer {

    private final HashMap<String, Object> singletonMap;
    private final HashMap<String, BeanClassRegistration> registeredClassMap;

    private MyContainer() {
        registeredClassMap = new HashMap<>();
        singletonMap = new HashMap<>();
    }

    public static MyContainer getContainer() {
        return new MyContainer();
    }

    public <T> void registerSingleton(Class<T> classType) {
        this.registerSingleton(classType, classType);
    }

    public <T, U extends T> void registerSingleton(Class<T> interfaceType, Class<U> implementationType) {
        registerType(interfaceType.getTypeName(), implementationType, BeanType.SINGLETON);
    }

    public <T> void registerPrototype(Class<T> classType) {
        this.registerPrototype(classType, classType);
    }

    public <T, U extends T> void registerPrototype(Class<T> interfaceType, Class<U> implementationType) {
        registerType(interfaceType.getTypeName(), implementationType, BeanType.PROTOTYPE);
    }

    public final <T> T createBean(Class<T> classType) {
        return createBean(classType, null);
    }

    @SneakyThrows
    private void registerType(String interfaceType, Type implementationType, BeanType beanType) {
        Class<?> registerClassType = Class.forName(implementationType.getTypeName());

        if (Modifier.isInterface(registerClassType.getModifiers())) {
            System.err.println(implementationType.getTypeName() + " is of type Interface. Interface not allowed to create directly.");
            throw new IllegalArgumentException(implementationType.getTypeName() + " is of type Interface. Interface not allowed to create directly.");
        }

        if (Modifier.isAbstract(registerClassType.getModifiers())) {
            System.err.println(implementationType.getTypeName() + " is of type Abstract class. Abstract class not allowed to create directly.");
            throw new IllegalArgumentException(implementationType.getTypeName() + " is of type Abstract class. Abstract class not allowed to create directly.");
        }

        if (!registeredClassMap.containsKey(interfaceType)) {
            registeredClassMap.put(interfaceType, BeanClassRegistration
                    .builder()
                    .beanType(beanType)
                    .classType(registerClassType)
                    .build());
        }
    }

    private <T> boolean isTypeRegistered(Class<T> classType) {
        return registeredClassMap.containsKey(classType.getTypeName());
    }

    private <T> T getBean(Type classType, BeanType beanType) {
        switch (beanType) {
            case SINGLETON:
                return getSingletonBean(classType.getTypeName());
            case PROTOTYPE:
            default:
                return null;
        }
    }

    private <T> T getSingletonBean(String classTypeName) {
        return (T) singletonMap.get(classTypeName);
    }

    private void registerBean(String classTypeName, BeanType beanType, Object object) {
        switch (beanType) {
            case SINGLETON:
                singletonMap.put(classTypeName, object);
                return;
            case PROTOTYPE:
            default:
                return;
        }
    }

    @SneakyThrows
    private <T> T createBean(Class<T> classType, HashSet<String> dependencySet) {

        if (!isTypeRegistered(classType)) {
            System.err.println(classType.getTypeName() + " Class Not registered. First register the class");
            throw new IllegalArgumentException(classType.getTypeName() + " Class Not registered. First register the class");
        }

        BeanClassRegistration classRegistration = registeredClassMap.get(classType.getTypeName());
        T previouslyCreatedObject = getBean(classRegistration.getClassType(), classRegistration.getBeanType());
        if (previouslyCreatedObject != null) {
            return previouslyCreatedObject;
        }

        String requestedClassTypeName = classType.getTypeName();
        String deliveredClassTypeName = classRegistration.getClassType().getTypeName();

        if (dependencySet == null) {
            dependencySet = new HashSet<>();
        }

        if (dependencySet.contains(requestedClassTypeName)) {
            System.err.println(requestedClassTypeName + " is having Circular Dependency which is not supported");
            throw new IllegalArgumentException(requestedClassTypeName + " is having Circular Dependency which is not supported");
        }

        dependencySet.add(requestedClassTypeName);

        Constructor<?>[] constructors = classRegistration.getClassType().getConstructors();

        if (constructors.length == 0) {
            System.err.println(deliveredClassTypeName + " No public constructor found. Provide one public constructor");
            throw new IllegalArgumentException(deliveredClassTypeName + " No public constructor found. Provide one public constructor");
        }

        for (Constructor<?> constructor : constructors) {

            List<Object> constructorParameters = new ArrayList<>();

            for (Parameter parameter : constructor.getParameters()) {
                // recursively call to form the dependency graph
                Object objectParameter = createBean(parameter.getType(), dependencySet);
                constructorParameters.add(objectParameter);
            }

            T myObj = (T) constructor.newInstance(constructorParameters.toArray());

            registerBean(requestedClassTypeName, classRegistration.getBeanType(), myObj);

            return myObj;
        }

        return null;
    }

}
