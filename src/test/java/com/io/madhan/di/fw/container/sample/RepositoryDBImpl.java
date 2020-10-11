package com.io.madhan.di.fw.container.sample;

public class RepositoryDBImpl implements RepositoryI {

    private final RepositoryServiceType TYPE = RepositoryServiceType.DB;

    {
        System.out.println(this.getClass().getTypeName() + " Initialized");
    }

    @Override
    public RepositoryServiceType getType() {
        return TYPE;
    }
}
