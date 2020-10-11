package com.io.madhan.di.fw.container.sample;

public class DependencyTest01 {

    {
        System.out.println(this.getClass().getTypeName() + " Initialized");
    }

    public DependencyTest01(TestObject01 test) {

    }
}
