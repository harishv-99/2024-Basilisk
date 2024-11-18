package org.firstinspires.ftc.teamcode.robots;


import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import edu.ftcphoenix.fw.gamepad.GamepadController;
import edu.ftcphoenix.fw.gamepad.GamepadInputs;
import edu.ftcphoenix.fw.gamepad.GamepadKeys;
import edu.ftcphoenix.fw.robotbase.periodicrunner.PeriodicRunner;

@TeleOp
public class TestHansikaSimple extends LinearOpMode {


    public void runOpMode() {

        PeriodicRunner periodicRunner = new PeriodicRunner();

        GamepadInputs gamepadInputs = new GamepadInputs();
        GamepadController gamepadController = new GamepadController(gamepad1, gamepadInputs,
                periodicRunner);
        gamepadController.createButton("one", GamepadKeys.Trigger.RIGHT_TRIGGER, 0.8);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        if (isStopRequested()) return;

        while (opModeIsActive()) {
            periodicRunner.runAllPeriodicRunnables();

//            telemetry.addData("isDown", gamepadReader.getButton("one").isDown() ? "1" : "0");

            if (gamepadInputs.getButton("one").wasJustReleased()) {
                telemetry.addData("hasReleased", "1");
                telemetry.update();
            }
            if (gamepadInputs.getButton("one").wasJustPressed()) {
                telemetry.addData("hasPressed", "1");
                telemetry.update();
            }
        }
    }


    public void runOpMode2() {
        DcMotor backleft;
        DcMotor frontleft;
        DcMotor backright;
        DcMotor frontright;

        backleft = hardwareMap.get(DcMotor.class, "backleft");
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        backright = hardwareMap.get(DcMotor.class, "backright");
        frontright = hardwareMap.get(DcMotor.class, "frontright");

        backright.setDirection(DcMotorSimple.Direction.REVERSE);
        backleft.setDirection(DcMotorSimple.Direction.REVERSE);


        // Wait for the game to start (driver presses PLAY)
        waitForStart();


        if (isStopRequested()) return;

        while (opModeIsActive()) {
            double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
            double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
            double rx = gamepad1.right_stick_x;

            // Denominator is the largest motor power (absolute value) or 1
            // This ensures all the powers maintain the same ratio,
            // but only if at least one is out of the range [-1, 1]
            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;

            frontleft.setPower(frontLeftPower);
            backleft.setPower(backLeftPower);
            frontright.setPower(frontRightPower);
            backright.setPower(backRightPower);

            telemetry.addData("y-value: ", y);
            telemetry.addData("x-value: ", x);
            telemetry.update();
        }
    }

/*
    public void runOpMode4() {
        DriveTrain driveTrain = new DriveTrainBuilder(hardwareMap, "frontleft",
                "frontright", "backleft",
                "backright")
                .reverseMotorBackLeft()
                .reverseMotorFrontLeft()
                .reverseMotorFrontRight()
                .useImu("imu",
                        RevHubOrientationOnRobot.LogoFacingDirection.FORWARD,
                        RevHubOrientationOnRobot.UsbFacingDirection.UP)
                .build();

        GamepadReader gamepadReader1 = new GamepadReader(gamepad1);
        RobotViewGamepadInterpreter gamepadInterpreter = new RobotViewGamepadInterpreter(gamepadReader1);
//        FieldViewGamepadInterpreter gamepadInterpreter = new FieldViewGamepadInterpreter(gamepadReader1,
//                driveTrain.getImu());

        waitForStart();

        if (isStopRequested()) return;
        while (opModeIsActive()) {
            gamepadReader1.processLoop();

            driveTrain.setDrivePower(gamepadInterpreter);
        }
    }
 */
}

/*
class ForwardDriveGamepadInterpreter implements DriveTrainGamepadInterpreter {

    private final GamepadReader gamepadReader;

    ForwardDriveGamepadInterpreter(GamepadReader gamepadReader) {
        this.gamepadReader = gamepadReader;
    }

    @Override
    public DrivePowerValues getDrivePowerValues() {
        double y = -gamepadReader.gamepad.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepadReader.gamepad.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepadReader.gamepad.right_stick_x;

        return new DrivePowerValues(x, y, rx);
    }
}


class RobotViewGamepadInterpreter implements DriveTrainGamepadInterpreter {

    private final GamepadReader gamepadReader;

    RobotViewGamepadInterpreter(GamepadReader gamepadReader) {
        this.gamepadReader = gamepadReader;
    }

    @Override
    public DrivePowerValues getDrivePowerValues() {
        double y = -gamepadReader.gamepad.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepadReader.gamepad.left_stick_x * 1.1; // Counteract imperfect strafing
        double rx = gamepadReader.gamepad.right_stick_x;

        return new DrivePowerValues(x, y, rx);
    }
}


class FieldViewGamepadInterpreter implements DriveTrainGamepadInterpreter {

    private final GamepadReader gamepadReader;
    private final IMU imu;

    FieldViewGamepadInterpreter(GamepadReader gamepadReader, IMU imu) {
        this.gamepadReader = gamepadReader;
        this.imu = imu;
    }

    @Override
    public DrivePowerValues getDrivePowerValues() {
        double y = -gamepadReader.gamepad.left_stick_y; // Remember, Y stick value is reversed
        double x = gamepadReader.gamepad.left_stick_x;
        double rx = gamepadReader.gamepad.right_stick_x;

        double botHeading = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);

        // Rotate the movement direction counter to the bot's rotation
        double rotX = x * Math.cos(-botHeading) - y * Math.sin(-botHeading);
        double rotY = x * Math.sin(-botHeading) + y * Math.cos(-botHeading);

        rotX = rotX * 1.1;  // Counteract imperfect strafing

        return new DrivePowerValues(rotX, rotY, rx);
    }
}
 */
