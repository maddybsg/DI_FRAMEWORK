package com.io.madhan.di.fw.container.sample;

public class RepositoryFileImpl implements RepositoryI {

    private final RepositoryServiceType TYPE = RepositoryServiceType.FILE;

    {
        System.out.println(this.getClass().getTypeName() + " Initialized");
    }

    @Override
    public RepositoryServiceType getType() {
        return TYPE;
    }
}
