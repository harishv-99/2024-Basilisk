package org.firstinspires.ftc.teamcode.robots;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import edu.ftcphoenix.fw.robotbase.RobotBase;
import edu.ftcphoenix.robots.phoenix.Robot;

@Disabled
@TeleOp
public class TestHansika extends LinearOpMode {
    Robot robot;

    public void runOpMode() {
        robot = new Robot(this, RobotBase.OpModeType.TELEOP,
                RobotBase.AllianceColor.RED,
                RobotBase.StartPosition.AWAY_FROM_AUDIENCE);
        robot.runOpMode();
    }
}