package com.luneo7.junit4.intellij.listeners;

import com.intellij.rt.execution.junit.IDEAJUnitListenerEx;
import org.junit.runner.Description;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JUnitListener implements IDEAJUnitListenerEx {
    private static final String PARAMETER_NAME = "jlisteners";
    private final List<Object> listeners = new ArrayList<>();

    public JUnitListener() {
        String property = System.getProperty(PARAMETER_NAME);
        if (property != null && !property.trim().isEmpty()) {
            Arrays.stream(property.split(","))
                  .map(String::trim)
                  .filter(c -> !c.isEmpty())
                  .forEach(c -> {
                      try {
                          listeners.add(Class.forName(c).newInstance());
                      } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
                          System.out.println("Class " + c + " was not found, not listener will be used for that class");
                      }
                  });
        }
    }

    @Override
    public void testRunStarted(String name) {
        listeners.forEach(l -> {
            if (l instanceof RunListener) {
                try {
                    ((RunListener) l).testRunStarted(Description.createSuiteDescription(name));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void testRunFinished(String name) {
        listeners.forEach(l -> {
            if (l instanceof RunListener) {
                try {
                    ((RunListener) l).testRunFinished(new org.junit.runner.Result());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void testFinished(String className, String methodName, boolean succeed) {
        if (succeed) {
            testFinished(className, methodName);
        } else {
            listeners.forEach(l -> {
                if (l instanceof RunListener) {
                    try {
                        ((RunListener) l).testFailure(
                                new Failure(Description.createTestDescription(className, methodName), new RuntimeException()));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }
    }

    @Override
    public void testStarted(String className, String methodName) {
        listeners.forEach(l -> {
            if (l instanceof RunListener) {
                try {
                    ((RunListener) l).testStarted(Description.createTestDescription(className, methodName));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Override
    public void testFinished(String className, String methodName) {
        listeners.forEach(l -> {
            if (l instanceof RunListener) {
                try {
                    ((RunListener) l).testFinished(Description.createTestDescription(className, methodName));
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}
