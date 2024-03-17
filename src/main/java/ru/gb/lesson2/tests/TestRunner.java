package ru.gb.lesson2.tests;

import java.lang.reflect.AccessFlag;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {

    public static void run(Class<?> testClass) {
        final Object testObj = initTestObj(testClass);
        Method[] methods = testClass.getDeclaredMethods();
        List<Method> testMethods = new ArrayList<>();
        List<Method> beforeAllMethods = new ArrayList<>();
        List<Method> beforeEachMethods = new ArrayList<>();
        List<Method> afterEachMethods = new ArrayList<>();
        List<Method> afterAllMethods = new ArrayList<>();
        for (Method testMethod : testClass.getDeclaredMethods()) {
            if (testMethod.accessFlags().contains(AccessFlag.PRIVATE)) {
                continue;
            }

            if (testMethod.getAnnotation(Test.class) != null) {
                try {
                    testMethod.invoke(testObj);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        // Get all methods with custom annotations
        for (Method method : methods) {
            if (method.isAnnotationPresent(Test.class)) {
                testMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeAll.class)) {
                beforeAllMethods.add(method);
            }
            if (method.isAnnotationPresent(BeforeEach.class)) {
                beforeEachMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterEach.class)) {
                afterEachMethods.add(method);
            }
            if (method.isAnnotationPresent(AfterAll.class)) {
                afterAllMethods.add(method);
            }
        }

        try {
            // Execute BeforeAll methods
            for (Method method : beforeAllMethods) {
                method.invoke(null);
            }

            // Execute BeforeEach -> Test -> AfterEach for each test method
            for (Method testMethod : testMethods) {
                for (Method beforeEachMethod : beforeEachMethods) {
                    beforeEachMethod.invoke(null);
                }
                testMethod.invoke(null);
                for (Method afterEachMethod : afterEachMethods) {
                    afterEachMethod.invoke(null);
                }
            }

            // Execute AfterAll methods
            for (Method method : afterAllMethods) {
                method.invoke(null);
            }

        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
    private static Object initTestObj(Class<?> testClass) {
        try {
            Constructor<?> noArgsConstructor = testClass.getConstructor();
            return noArgsConstructor.newInstance();
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Нет конструктора по умолчанию");
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("Не удалось создать объект тест класса");
        }
    }
}
