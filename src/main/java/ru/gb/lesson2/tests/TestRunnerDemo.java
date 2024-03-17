import ru.gb.lesson2.tests.*;

import static ru.gb.lesson2.tests.TestRunner.run;

public static class SampleTestClass {

    @BeforeAll
    public static void initAll() {
        System.out.println("BeforeAll tests");
    }

    @AfterAll
    public static void cleanUpAll() {
        System.out.println("AfterAll tests");
    }

    @BeforeEach
    public static void init() {
        System.out.println("BeforeEach test");
    }

    @AfterEach
    public static void cleanUp() {
        System.out.println("AfterEach test");
    }

    @Test
    public static void testMethod1() {
        System.out.println("Running test method 1");
    }

    @Test
    public static void testMethod2() {
        System.out.println("Running test method 2");
    }

    // Add more test methods as needed
}

// Main method to run tests
public static void main(String[] args) {
    run(SampleTestClass.class);
}