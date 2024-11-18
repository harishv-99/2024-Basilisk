package org.firstinspires.ftc.teamcode.testcomponents;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp
public class MotorProfiler extends LinearOpMode {

    public void runOpMode() {
        DcMotorEx backleft;
        DcMotorEx frontleft;
        DcMotorEx backright;
        DcMotorEx frontright;

        backleft = hardwareMap.get(DcMotorEx.class, "backleft");
        frontleft = hardwareMap.get(DcMotorEx.class, "frontleft");
        backright = hardwareMap.get(DcMotorEx.class, "backright");
        frontright = hardwareMap.get(DcMotorEx.class, "frontright");

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (isStopRequested()) return;

//        testMotorProfile(backleft, "backleft");
//        if (isStopRequested()) return;
        testMotorProfile(frontleft, "frontleft");
//        if (isStopRequested()) return;
//        testMotorProfile(backright, "backright");
//        if (isStopRequested()) return;
//        testMotorProfile(frontright, "frontright");
    }

    void testMotorProfile(DcMotorEx motor, String motorName) {
        pauseForKeyPress1();

        testMotorProfileAtPower(motor, motorName, 0.25);
        testMotorProfileAtPower(motor, motorName, 1);
    }

    void testMotorProfileAtPower(DcMotorEx motor, String motorName, double power) {
        testMotorVelocity(motor, motorName, power);
        pauseForKeyPress1();

        testMotorStopTime(motor, motorName, power);
        pauseForKeyPress2();
    }

    void testMotorStopTime(DcMotorEx motor, String motorName, double power) {
        ElapsedTime elapsedTime = new ElapsedTime();

        // Set the power to 0
        motor.setPower(0);

        // Capture the velocity as it ramps down speed
        double velCurrent = 0.0;

        // As the motor is decreasing its velocity, capture the amount of time it takes
        do {
            // Pause for a while while we wait for the motor to continue to react to the
            //    command.
            sleep(25);

            // Get a read on the new velocity
            velCurrent = motor.getVelocity();

            // Capture the stats -- max power, time since start, current velocity.
            captureStats(motorName, power, elapsedTime.milliseconds(), false,
                    velCurrent);

            // NOTE: We continue as long as the velocity is decreasing.
        } while (velCurrent > 0);
    }

    void testMotorVelocity(DcMotorEx motor, String motorName, double power) {
        ElapsedTime elapsedTime = new ElapsedTime();

        // Set the power
        motor.setPower(power);

        // Capture the velocity as it ramps up speed
        double velPrior;
        double velCurrent = 0.0;

        // As the motor is increasing its velocity, capture the amount of time
        do {
            velPrior = velCurrent;

            // Pause for a while while we wait for the motor to continue to react to the
            //    command.
            sleep(100);

            // Get a read on the new velocity
            velCurrent = motor.getVelocity();

            // Capture the stats -- max power, time since start, current velocity.
            captureStats(motorName, power, elapsedTime.milliseconds(), true,
                    velCurrent);

            // NOTE: We continue as long as the velocity is increasing.
        } while (velPrior < velCurrent);
    }


    void captureStats (String motorName,double maxPower, double elapsedMillis,
                       boolean isAccelerating, double velocity){
        telemetry.addData("Motor Name", motorName);
        telemetry.addData("Max Power", maxPower);
        telemetry.addData("Elapsed millis", elapsedMillis);
        telemetry.addData("Is Accelerating", isAccelerating);
        telemetry.addData("Velocity", velocity);
        telemetry.update();
    }

    void pauseForKeyPress1() {
        while (!gamepad1.a) {
            if (isStopRequested()) return;
            sleep(10);
        }
    }
    void pauseForKeyPress2() {
        while (!gamepad1.b) {
            if (isStopRequested()) return;
            sleep(10);
        }
    }
}


