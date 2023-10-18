package com.github.ksewen.yorozuya.common.context.impl;

import com.github.ksewen.yorozuya.common.context.Context;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import javax.annotation.Nullable;

/**
 * @author ksewen
 * @date 13.10.2023 13:02
 */
public class ServiceContext implements Context {

  private final ThreadLocal<Container> context = new ThreadLocal<>();

  private final ReadWriteLock rwLock = new ReentrantReadWriteLock(true);

  public static ServiceContext getInstance() {
    return INSTANCE;
  }

  private static final ServiceContext INSTANCE = new ServiceContext();

  private ServiceContext() {}

  @Override
  public void put(String key, String value) {
    this.getCurrentContext().put(key, value);
  }

  @Override
  public void putAll(Map<String, String> map) {
    this.getCurrentContext().putAll(map);
  }

  @Nullable
  @Override
  public String get(String key) {
    return this.getCurrentContext().get(key);
  }

  @Override
  public void remove(String key) {
    this.getCurrentContext().remove(key);
  }

  @Override
  public Map<String, String> getContext() {
    return this.getCurrentContext().getContext();
  }

  @Override
  public boolean contains(String key) {
    return this.getCurrentContext().contains(key);
  }

  @Override
  public void shutdown() {
    this.context.remove();
  }

  private Container getCurrentContext() {
    rwLock.writeLock().lock();
    try {
      if (context.get() == null) {
        context.set(new Container());
      }
    } finally {
      rwLock.writeLock().unlock();
    }
    return this.context.get();
  }

  static class Container {
    private Map<String, String> map = new ConcurrentHashMap<>();

    public void put(String key, String value) {
      this.map.put(key, value);
    }

    public void putAll(Map<String, String> m) {
      this.map.putAll(m);
    }

    public String get(String key) {
      return this.map.get(key);
    }

    public void remove(String key) {
      this.map.remove(key);
    }

    public Map<String, String> getContext() {
      return this.map;
    }

    public boolean contains(String key) {
      return this.map.containsKey(key);
    }

    public void clear() {
      this.map.clear();
    }
  }
}
