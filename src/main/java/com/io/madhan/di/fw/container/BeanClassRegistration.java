package com.io.madhan.di.fw.container;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Getter
@Builder
public class BeanClassRegistration {

    @NonNull
    private final Class<?> classType;

    @NonNull
    private final BeanType beanType;
}
