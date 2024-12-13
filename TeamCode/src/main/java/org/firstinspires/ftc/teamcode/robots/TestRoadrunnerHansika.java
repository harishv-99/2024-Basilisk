package org.firstinspires.ftc.teamcode.robots;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Rotation2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.TrajectoryActionBuilder;
import com.acmerobotics.roadrunner.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;


import org.firstinspires.ftc.teamcode.MecanumDrive;

import edu.ftcphoenix.robots.phoenix.subsystems.ArmSubsystem;



@Config
@Autonomous(name = "TestRoadrunnerHansika")
public class TestRoadrunnerHansika extends LinearOpMode {

    ArmSubsystem arm;
    int nPosStart = 1;
    int nPosPark = 1;
    @Override
    public void runOpMode() {
        arm = new ArmSubsystem(hardwareMap, telemetry);
        Pose2d initialPose;
        if(nPosStart == 0) {
            initialPose = new Pose2d(32, 6, Math.toRadians(180));
        }
        else {
            initialPose = new Pose2d(86, 6, Math.toRadians(90));
        }
        MecanumDrive drive = new MecanumDrive(hardwareMap, initialPose);

        TrajectoryActionBuilder tabStartToBasket;
        if (nPosStart == 0) {
            tabStartToBasket = drive.actionBuilder(initialPose)
                    .strafeToSplineHeading(new Vector2d(5, 17), Math.toRadians(180+45));
        }
        else {
            // TODO
            tabStartToBasket = drive.actionBuilder(initialPose)
                    .strafeToSplineHeading(new Vector2d(7, 19), Math.toRadians(180+45));
        }

        TrajectoryActionBuilder tabAwayFromBasket = tabStartToBasket.endTrajectory().fresh()
                .strafeTo(new Vector2d(23, 23));

        TrajectoryActionBuilder tabAwayToParkPrepare;
        if(nPosStart == 0) {
            if(nPosPark == 0) {
                tabAwayToParkPrepare = tabAwayFromBasket.endTrajectory().fresh()
                        .strafeToSplineHeading(new Vector2d(125, 33), Math.toRadians(90))
                        .strafeToSplineHeading(new Vector2d(125, 23), Math.toRadians(90));
            }
            // PARK TO RIGHT
            else {
                tabAwayToParkPrepare = tabAwayFromBasket.endTrajectory().fresh()
                        .strafeToSplineHeading(new Vector2d(148, 33), Math.toRadians(90))
                        .strafeToSplineHeading(new Vector2d(148, 23), Math.toRadians(90));
            }
        }
        else {
            tabAwayToParkPrepare = drive.actionBuilder(initialPose)
                    .strafeToSplineHeading(new Vector2d(148, 11), Math.toRadians(90));
                    //.strafeToSplineHeading(new Vector2d(148, 23), Math.toRadians(90));
        }


        while (!isStopRequested() && !opModeIsActive()) {
            telemetry.addData("Position during Init", 0);
            telemetry.update();
        }

        waitForStart();

        if (isStopRequested()) return;

        Action actRaiseArmToBucketHigh = new ParallelAction(
                arm.getArmExtenderAction(1, 1_800),
                arm.getMoveSlidesAction(20_000),
                arm.getMoveArmRaiserAction(1_100)
        );

        Action actReleaseSample = arm.getMoveRollerIntakeAction(-1, 2000);
        Action actLowerSlides = arm.getMoveSlidesAction(0);

        Action act;
        if(nPosStart == 0) {
            act = new SequentialAction(
                    actRaiseArmToBucketHigh,
                    tabStartToBasket.build(),
                    actReleaseSample,
                    tabAwayFromBasket.build(),
                    actLowerSlides,
                    tabAwayToParkPrepare.build()
            );
        }
        else {
            act = tabAwayToParkPrepare.build();
        }

//        Action act = tabStartToBasket.build();

        Actions.runBlocking(act);
    }
}
