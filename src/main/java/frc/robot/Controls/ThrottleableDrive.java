package frc.robot.Controls;

public class ThrottleableDrive extends TwoStickDriveXboxOp{
    public ThrottleableDrive(int LeftPort, int RightPort, int opPort){
        super(LeftPort, RightPort, opPort);
    }
    @Override
    public double Throttle(){
        return 1-LeftStick.getThrottle();
    }

    public double Throttle2(){
        return 1-RightStick.getThrottle();
    }
    @Override
    public double InputLeft() {
        return deadzone(-LeftStick.getY() )* Throttle();
    }

    @Override
    public double InputUp() {
        return deadzone(-LeftStick.getX() ) * Throttle();
    }

    @Override
    public double InputTheta() {
        return deadzone(-RightStick.getX() ) * Throttle();
    }
}