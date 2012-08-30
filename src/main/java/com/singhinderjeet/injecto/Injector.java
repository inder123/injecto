/*
 * Copyright (C) 2011 Inderjeet Singh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.singhinderjeet.injecto;

import java.util.HashMap;
import java.util.Map;

/**
 * The primary class for dependency injection. You will first add instances
 * or providers for instances in the Injector, and then can get them subsequently.
 *
 * @author Inderjeet Singh
 */
public final class Injector {

  public static final class Builder {
    private static final int INITIAL_CAPACITY = 12;
    
    private final Map<Class<?>, Provider<?>> providers = new HashMap<Class<?>, Provider<?>>(12);
    private final Map<Class<?>, Object> instances = new HashMap<Class<?>, Object>(INITIAL_CAPACITY);
    public <T> Builder addProvider(Class<T> clazz, Provider<T> provider) {
      providers.put(clazz, provider);
      return this;
    }

    public <T> Builder addInstance(Class<T> clazz, T instance) {
      instances.put(clazz, instance);
      return this;
    }

    public <T> Provider<T> getProvider(Class<T> clazz) {
      @SuppressWarnings("unchecked")
      Provider<T> provider = (Provider<T>) providers.get(clazz);
      return provider;
    }

    public Injector create() {
      return new Injector(providers, instances);
    }
  }

  private final Map<Class<?>, Provider<?>> providers;
  private final Map<Class<?>, Object> instances;
  
  private Injector(Map<Class<?>, Provider<?>> providers, Map<Class<?>, Object> instances) {
    this.providers = providers;
    this.instances = instances;
  }
  
  @SuppressWarnings("unchecked")
  public <T> T getInstance(Class<T> classOfT) {
    T instance = (T) instances.get(classOfT);
    if (instance == null) {
      Provider<T> provider = getProvider(classOfT);
      if (provider != null) {
        instance = provider.get();
      }
    }
    return instance;
  }
  
  @SuppressWarnings("unchecked")
  public <T> Provider<T> getProvider(Class<T> classOfT) {
    return (Provider<T>)providers.get(classOfT);
  }
}
