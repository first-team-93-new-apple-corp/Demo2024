package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.phoenix6.hardware.TalonFX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase {
    LEDSubsystem m_LED;
    TalonFX ShooterR = new TalonFX(17);
    TalonFX ShooterL = new TalonFX(16);
    static CANSparkMax KickerL;
    static CANSparkMax KickerR;
    double SpeakerShooterSpeed = 0.55;
    double currentspeed;
    final double MuzzleIntake = -0.1;
    final double AmpShooterSpeed = 0.1;
    final double KickerSpeed = 1;
    final double DribbleSpeed = .25;
    double StartTime;

    public ShooterSubsystem(LEDSubsystem m_LED) {
        this.m_LED = m_LED;
        if (KickerL == null || KickerR == null) {
            KickerL = new CANSparkMax(18, MotorType.kBrushless);
            KickerR = new CANSparkMax(19, MotorType.kBrushless);
        }
        ShooterL.setInverted(true);
        ShooterR.setInverted(false);
        KickerR.setInverted(true);
    }

    public void shoot(double speed) {
        ShooterR.set(speed);
        ShooterL.set(speed);
    }

    public void prime() {
        ShooterR.set(SpeakerShooterSpeed);
        ShooterL.set(SpeakerShooterSpeed);
    }

    public void AutonPrime(){
        ShooterL.set(.60);
        ShooterR.set(.60);
    }

    public void shootAmp() {
        ShooterR.set(AmpShooterSpeed);
        ShooterL.set(AmpShooterSpeed);
    }

    public void kicker(double KickerSpeed) {
        KickerL.set(KickerSpeed);
        KickerR.set(KickerSpeed);
    }

    public void intakeFront() {
        ShooterR.set(MuzzleIntake);
        ShooterL.set(MuzzleIntake);
        KickerL.set(-0.05);
        KickerR.set(-0.05);
    }

    public void shooterStop() {
        ShooterR.set(0);
        ShooterL.set(0);
        KickerL.set(0);
        KickerR.set(0);
    }

    public void kickerStop() {
        KickerL.set(0);
        KickerR.set(0);
    }
    public void ampKicker() {
        KickerL.set(0.25);
        KickerR.set(0.25);
    }
    public void AmpForAuton(){
        shootAmp();
        kicker(KickerSpeed);
        // m_LED.turnLEDSOff();
    }
    public void ShootingforAuton() {
        AutonPrime();
        kicker(KickerSpeed);
        // m_LED.turnLEDSOff();
    }

    public void DribbleOutNote() {
        shoot(DribbleSpeed);
        kicker(DribbleSpeed);
    }

    public Command AutonAmp() {
        return this.runOnce(() -> AmpForAuton());
    }

    public Command AutonShooter() {
        return this.runOnce(() -> ShootingforAuton());
    }

    public Command AutonStopShooter() {
        return this.runOnce(() -> shooterStop());
    }

    public Command AutonDribbleNote(){
        return this.runOnce(() -> DribbleOutNote());
    }

    public void increaseSpeed() {
        if (SpeakerShooterSpeed <= 0.95) {
            SpeakerShooterSpeed += 0.05; // +5%speed
        }
        // SmartDashboard.putNumber("CurrentSpeed", SpeakerShooterSpeed);

    }

    public void decreaseSpeed() {
        if (SpeakerShooterSpeed >= 0.1) {
            SpeakerShooterSpeed -= 0.05; // -5%speed
        }
        // SmartDashboard.putNumber("CurrentSpeed", SpeakerShooterSpeed);

    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Shooter Speed", SpeakerShooterSpeed);
        SmartDashboard.putNumber("Shooter L RPM", ShooterR.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Shooter R RPM", ShooterR.getVelocity().getValueAsDouble());
        SmartDashboard.putNumber("Shooter L Temp", ShooterL.getDeviceTemp().getValueAsDouble());
        SmartDashboard.putNumber("Shooter R Temp", ShooterR.getDeviceTemp().getValueAsDouble());
    }
}