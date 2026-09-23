package frc.robot.subsystems;

import frc.robot.Constants;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix6.hardware.DeviceIdentifier;
import com.ctre.phoenix6.hardware.TalonFX;
import com.ctre.phoenix6.signals.MotorAlignmentValue;
import com.ctre.phoenix6.signals.InvertedValue;
import com.ctre.phoenix6.configs.TalonFXConfiguration;
import com.ctre.phoenix6.controls.ControlRequest;
import com.ctre.phoenix6.controls.Follower;
import com.revrobotics.PersistMode;
import com.revrobotics.ResetMode;
import com.revrobotics.spark.SparkMax;
import com.revrobotics.spark.config.SparkMaxConfig;
import com.revrobotics.spark.SparkLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase {
    LEDSubsystem m_LED;
    TalonFX ShooterR = new TalonFX(Constants.ShooterConstants.ShooterR);
    TalonFX ShooterL = new TalonFX(Constants.ShooterConstants.ShooterL);
    TalonFXConfiguration configL = new TalonFXConfiguration();
    static SparkMax KickerL;
    static SparkMax KickerR;
    SparkMaxConfig krConfig = new SparkMaxConfig();
    static double SpeakerShooterSpeed = 0.55;
    static double currentspeed;
    static double MuzzleIntake = -0.1;
    final static double AmpShooterSpeed = 0.1;
    final static double KickerSpeed = 1;
    final static double DribbleSpeed = .25;
    double StartTime;

    public ShooterSubsystem() {
        if (KickerL == null || KickerR == null) {
            KickerL = new SparkMax(18, MotorType.kBrushless);
            KickerR = new SparkMax(19, MotorType.kBrushless);
        }
        configL.MotorOutput.Inverted = InvertedValue.Clockwise_Positive;
        ShooterL.getConfigurator().apply(configL);
        // ShooterL.setControl(new )
        krConfig.inverted(true);
        KickerR.configure(krConfig, ResetMode.kResetSafeParameters, PersistMode.kPersistParameters);
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

    // public Command Shoot() {
    //     return this.runOnce(() -> shoot(SpeakerShooterSpeed));
    // }

    public Command Shoot() {
        return this.runOnce(() -> DribbleOutNote());
    }

    public Command Stop() {
        return this.runOnce(() -> shooterStop());
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