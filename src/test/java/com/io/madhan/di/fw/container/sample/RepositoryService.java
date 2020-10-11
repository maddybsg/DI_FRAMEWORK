package com.io.madhan.di.fw.container.sample;

public class RepositoryService {

    private final RepositoryI service;

    {
        System.out.println(this.getClass().getTypeName() + " Initialized");
    }

    public RepositoryService(RepositoryI service) {
        this.service = service;
    }

    public RepositoryI getService() {
        return service;
    }
}


