package edu.ftcphoenix.robots.phoenix;

import edu.ftcphoenix.robots.phoenix.controllers.teleop.TeleOpButtonDriveState;
import edu.ftcphoenix.robots.phoenix.controllers.teleop.TeleOpStickDriveState;

public class Constants {
    public static final String MOTOR_NAME_FRONT_LEFT = "frontleft";
    public static final String MOTOR_NAME_FRONT_RIGHT = "frontright";
    public static final String MOTOR_NAME_BACK_LEFT = "backleft";
    public static final String MOTOR_NAME_BACK_RIGHT = "backright";

    public static final String IMU_NAME = "imu";

    public static final String CONTROL_HUB_NAME = "Control Hub";

    public static final String INTERVAL_NAME_AXIAL = "interval_axial";
    public static final String INTERVAL_NAME_LATERAL = "interval_lateral";
    public static final String INTERVAL_NAME_ANGULAR = "interval_angular";

    public static final String BUTTON_NAME_MOVE = "move";
    public static final String BUTTON_NAME_STOP = "stop";


    public static final String TELEOP_BUTTON_DRIVE_STATE = TeleOpButtonDriveState.class.getSimpleName();
    public static final String TELEOP_STICK_DRIVE_STATE = TeleOpStickDriveState.class.getSimpleName();


    public static final double VOLTAGE_SENSOR_OBSTACLE_THRESHOLD = 9;
}
