/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */


import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Simple unit tests for AeroDashController
 */
public class SimpleTest {

    // CONSTANTS FROM AerodashController
    private static final double AIR_DENSITY = 1.225;
    private static final double CL = 1.2;
    private static final double CD = 0.02;

    // LIFT TEST

    /**
    * Tests basic lift calculation with normal parameters
    * Verifies that lift force is positive under normal flight conditions
    */
    @Test
    public void testLiftCalculation() {
        double velocity = 100.0;
        double wingArea = 20.0;
        double angleOfAttack = 10.0;
        
        double lift = calculateLift(velocity, wingArea, angleOfAttack);
        
        // Assertion: Lift should always be positive with these parameters
        assertTrue("Lift should be positive", lift > 0);
    }

    /**
    * Tests lift calculation when velocity is zero (stationary aircraft)
    * Verifies that no lift is generated when there's no airflow
    */
    @Test
    public void testLiftWithZeroVelocity() {
        double lift = calculateLift(0.0, 20.0, 10.0);
        // Assertion: Zero velocity should result in zero lift
        assertEquals(0.0, lift,0.01);
    }

    /**
     * Tests that lift increases with airspeed
     * Verifies the aerodynamic principle that lift is proportional to velocity squared
     */
    @Test
    public void testLiftIncreasesWithSpeed() {
        double lift50 = calculateLift(50.0, 20.0, 10.0);
        double lift100 = calculateLift(100.0, 20.0, 10.0);
        // Assertion: Higher speed should produce more lift
        assertTrue("Faster speed = more lift", lift100 > lift50);
    }

     /**
     * Tests that lift increases with wing area
     * Verifies the aerodynamic principle that lift is proportional to wing surface area
     */
    @Test
    public void testLiftIncreasesWithWingArea() {
        // Calculate lift with two different wing areas at same speed and angle
        double liftSmall = calculateLift(100.0, 10.0, 10.0);
        double liftLarge = calculateLift(100.0, 20.0, 10.0);
        // Assertion: Larger wing area should produce more lift
        assertTrue("Bigger wing = more lift", liftLarge > liftSmall);
    }

    /**
     * Tests lift calculation at zero angle of attack
     * Verifies that minimal lift is generated when wings are parallel to airflow
     */
    @Test
    public void testLiftAtZeroAngle() {
        double lift = calculateLift(100.0, 20.0, 0.0);
        
        // At 0 degrees, lift should be very small
        assertTrue("Lift at 0° should be small", Math.abs(lift) < 100);
    }

    //DRAG TESTS: 

    /**
     * Tests basic drag calculation with normal parameters
     * Verifies that drag force is positive under normal flight conditions
     */
    @Test
    public void testDragCalculation() {
        double velocity = 100.0;
        double wingArea = 20.0;
        
        double drag = calculateDrag(velocity, wingArea);
        // Assertion: Drag should always be positive (opposes motion)
        assertTrue("Drag should be positive", drag > 0);
    }

    /**
     * Tests drag calculation when velocity is zero
     * Verifies that no drag is generated when there's no motion
     */
    @Test
    public void testDragWithZeroVelocity() {
        double drag = calculateDrag(0.0, 20.0);
        // Assertion: Zero velocity should result in zero drag
        assertEquals(0.0, drag, 0.01);
    }

    /**
     * Tests that drag increases with airspeed
     * Verifies the aerodynamic principle that drag is proportional to velocity squared
     */
    @Test
    public void testDragIncreasesWithSpeed() {
        double drag50 = calculateDrag(50.0, 20.0);
        double drag100 = calculateDrag(100.0, 20.0);
        // Assertion: Higher speed should produce more drag
        assertTrue("Faster speed = more drag", drag100 > drag50);
    }
    
    /**
     * Tests that drag increases with wing area
     * Verifies the aerodynamic principle that drag is proportional to wing surface area
     */
    @Test
    public void testDragIncreasesWithWingArea() {
        double dragSmall = calculateDrag(100.0, 10.0);
        double dragLarge = calculateDrag(100.0, 20.0);
         // Assertion: Larger wing area should produce more drag
        assertTrue("Bigger wing = more drag", dragLarge > dragSmall);
    }

    /**
     * Tests the exact drag formula calculation
     * Verifies that the mathematical implementation matches the theoretical formula
     */
    @Test
    public void testDragFormula() {
        double velocity = 100.0;
        double wingArea = 20.0;
        
        // Expected value using the standard drag formula
        double expected = 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;
         // Actual value from our calculation method
        double actual = calculateDrag(velocity, wingArea);
        
        // Assertion: Calculated value should match expected formula result
        assertEquals(expected, actual, 0.01);
    }

    // COMPARISON TEST: 
    
     /**
     * Tests that lift force is greater than drag force under normal conditions
     * Verifies basic flight principle: for sustained flight, lift must exceed drag
     */
    @Test
    public void testLiftIsGreaterThanDrag() {
        double velocity = 100.0;
        double wingArea = 20.0;
        double angle = 15.0;
        
        double lift = calculateLift(velocity, wingArea, angle);
        double drag = calculateDrag(velocity, wingArea);
        
        // Assertion: Lift should exceed drag for successful flight
        assertTrue("Lift should be greater than drag", lift > drag);
    }

    /**
     * Tests that net vertical force is positive (upward)
     * Verifies that the aircraft can generate enough upward force to overcome gravity
     */
    @Test
    public void testNetForceIsPositive() {
        double velocity = 100.0;
        double wingArea = 20.0;
        double angle = 15.0;
        
        double lift = calculateLift(velocity, wingArea, angle);
        double drag = calculateDrag(velocity, wingArea);
        double netForce = lift - drag;
        
        // Assertion: Net force should be positive for climb or level flight
        assertTrue("Net force should be positive (upward)", netForce > 0);
    }

    //HELPER METHODS: 

    /*
    calculate lift
    */
    private double calculateLift(double velocity, double wingArea, double angleOfAttack) {
        double alpha = Math.toRadians(angleOfAttack);
        double cl = CL * Math.sin(2 * alpha);
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * cl;
    }

    /*
    calculate drag
    */
    private double calculateDrag(double velocity, double wingArea) {
        return 0.5 * AIR_DENSITY * velocity * velocity * wingArea * CD;
    }

    //boolean checking is angle is valid
    private boolean isAngleValid(double angle) {
        return angle >= -10.0 && angle <= 30.0;
    }
    //boolean chekcing if velocity is positive
    private boolean isVelocityValid(double velocity) {
        return velocity > 0;
    }
    //bollean checking if wing Area is positive
    private boolean isWingAreaValid(double wingArea) {
        return wingArea > 0;
    }
}
