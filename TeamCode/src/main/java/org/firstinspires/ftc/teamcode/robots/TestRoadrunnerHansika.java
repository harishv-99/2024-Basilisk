package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.CRServo;

import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.ftcphoenix.robots.phoenix.subsystems.ArmSubsystem;



@Config
@Autonomous(name = "TestRoadrunnerHansika")
public class TestRoadrunnerHansika extends LinearOpMode {

    ArmSubsystem arm;
    @Override
    public void runOpMode() {
//        CRServo armExtender2;
//        armExtender2 = hardwareMap.get(CRServo.class, "armExtender");
//        armExtender2.setDirection(CRServo.Direction.REVERSE);
//        armExtender2.setPower(1);
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
//        armExtender2.setPower(0);

        arm = new ArmSubsystem(hardwareMap, telemetry);
        arm.moveArmRaiser(500);
        arm.moveRollerIntake(1, 3000);
//        arm.moveArmExtender(-0.2, 1000);
        Pose2d initialPose = new Pose2d(0, 0, Math.toRadians(0));
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tab1 = drive.actionBuilder(initialPose)
                // score pre loaded sample
                .lineToX(-33)
                .turn(Math.toRadians(70));
//                .turn(Math.toRadians(70))
//                .lineToX(-36)
//                .waitSeconds(2)
//                .turn(Math.toRadians(165))
//                //.lineToY(3);
//                .strafeTo(new Vector2d(-50, 30));     // go to one sample
                // .turn(Math.toRadians(100));
                //.strafeTo(new Vector2d(-40, 5))    // go back to baskets
                //.splineTo(new Vector2d(-38, 40), (6 * Math.PI) / 5);
                // repeat for other 2 samples




                // score 3 samples on left
                //.splineTo(new Vector2d(40, -50), (6 * Math.PI) / 5);

        Action trajectoryActionCloseOut = tab1.endTrajectory().fresh()
                //.strafeTo(new Vector2d(48, 12))
                .build();


        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", 0);
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Action trajectoryActionChosen;
        trajectoryActionChosen = tab1.build();

        Action act = new ParallelAction(
//            trajectoryActionChosen,
//            arm.getArmExtenderAction(-1, 500)
            arm.getMoveRollerIntakeAction(1, 500)
//            arm.getMoveSlidesAction(500),
//            arm.getMoveArmRaiserAction(700)
        );

        Actions.runBlocking(act);
    }
}
