package org.firstinspires.ftc.teamcode.robots;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.robotcore.external.JavaUtil;

@TeleOp(name = "GameDayCode")
public class GameDayCode extends LinearOpMode {

    private DcMotor slideMotor;
    private DcMotor armRaiserMotor;
    private DcMotor frontleftMotor;
    private DcMotor backleftMotor;
    private DcMotor frontrightMotor;
    private DcMotor backrightMotor;
    private CRServo rollerIntake;
    private CRServo armExtender;

    double y;

    /**
     * This function is executed when this Op Mode is selected from the Driver Station.
     */
    @Override
    public void runOpMode() {
        slideMotor = hardwareMap.get(DcMotor.class, "slideMotor");
        armRaiserMotor = hardwareMap.get(DcMotor.class, "armRaiserMotor");
        frontleftMotor = hardwareMap.get(DcMotor.class, "frontleftMotor");
        backleftMotor = hardwareMap.get(DcMotor.class, "backleftMotor");
        frontrightMotor = hardwareMap.get(DcMotor.class, "frontrightMotor");
        backrightMotor = hardwareMap.get(DcMotor.class, "backrightMotor");
        rollerIntake = hardwareMap.get(CRServo.class, "rollerIntake");
        armExtender = hardwareMap.get(CRServo.class, "armExtender");

        // Reverse the right side motors.  This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards, reverse the left side instead.
        slideMotor.setDirection(DcMotor.Direction.FORWARD);
        slideMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        armRaiserMotor.setDirection(DcMotor.Direction.REVERSE);
        armRaiserMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontleftMotor.setDirection(DcMotor.Direction.REVERSE);
        backleftMotor.setDirection(DcMotor.Direction.REVERSE);
        frontrightMotor.setDirection(DcMotor.Direction.FORWARD);
        backrightMotor.setDirection(DcMotor.Direction.REVERSE);
        waitForStart();
        while (opModeIsActive()) {
            moveIntake();
            moveRobot();
            moveSlides();
            armExtenderServo();
            armRaiser();
            telemetry.update();
        }
    }

    /**
     * Describe this function...
     */
    private void moveIntake() {
        rollerIntake.setDirection(CRServo.Direction.REVERSE);
        if (gamepad2.y) {
            rollerIntake.setPower(1);
            telemetry.addLine("Intake Forward");
        } else if (gamepad2.a) {
            rollerIntake.setPower(-1);
            telemetry.addLine("Intake Reverse");
        } else {
            rollerIntake.setPower(0);
        }
    }

    /**
     * Describe this function...
     */
    private void armExtenderServo() {
        armExtender.setDirection(CRServo.Direction.REVERSE);
        if (gamepad2.dpad_up) {
            armExtender.setPower(1);
        } else if (gamepad2.dpad_down) {
            armExtender.setPower(-1);
        } else {
            armExtender.setPower(0);
        }
    }

    /**
     * Describe this function...
     */
    private void moveRobot() {
        double x;
        double rx;
        double denominator;

        // Remember, Y stick value is reversed
        y = -gamepad1.left_stick_y * 0.5;
        // Factor to counteract imperfect strafing
        x = gamepad1.left_stick_x * 0.5;
        rx = gamepad1.right_stick_x * 0.5;
        // Denominator is the largest motor power (absolute value) or 1.
        // This ensures all powers maintain the same ratio, but only if one is outside of the range [-1, 1].
        denominator = JavaUtil.maxOfList(JavaUtil.createListWith(JavaUtil.sumOfList(JavaUtil.createListWith(Math.abs(y), Math.abs(x), Math.abs(rx))), 1));
        // Make sure your ID's match your configuration
        frontleftMotor.setPower((y + x + rx) / denominator);
        backleftMotor.setPower(((y - x) + rx) / denominator);
        frontrightMotor.setPower(((y - x) - rx) / denominator);
        backrightMotor.setPower(((y + x) - rx) / denominator);
    }

    /**
     * Describe this function...
     */
    private void armRaiser() {
        float armExtenderUp;

        armExtenderUp = -gamepad2.right_stick_y * 50;
        telemetry.addData("Target", armRaiserMotor.getCurrentPosition() + armExtenderUp);
        telemetry.addData("armExtenderPosChange", armExtenderUp);
        if (armExtenderUp < 0 && armRaiserMotor.getCurrentPosition() > 0) {
            armRaiserMotor.setTargetPosition((int) (armRaiserMotor.getCurrentPosition() + armExtenderUp));
        } else if (armExtenderUp > 0 && armRaiserMotor.getCurrentPosition() < 1500) {
            armRaiserMotor.setTargetPosition((int) (armRaiserMotor.getCurrentPosition() + armExtenderUp));
        } else {
            armRaiserMotor.setTargetPosition(armRaiserMotor.getCurrentPosition());
        }
        armRaiserMotor.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armRaiserMotor.setPower(0.8);
    }

    /**
     * Describe this function...
     */
    private void moveSlides() {
        // Use gamepad_and will press_and _to move the slides up and down, height may vary
        // Remember, Y stick value is reversed
        telemetry.addData("SlidePos", slideMotor.getCurrentPosition());
        y = -gamepad2.left_stick_y * 1;
        telemetry.addData("SlidePower", y);
        if (y < 0 && slideMotor.getCurrentPosition() > 0) {
            slideMotor.setPower(y);
        } else if (y > 0 && slideMotor.getCurrentPosition() < 12000) {
            slideMotor.setPower(y);
        } else {
            slideMotor.setPower(0);
        }
    }
}