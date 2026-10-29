package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.DIO;

public class LEDSubsystem extends SubsystemBase {
    private static final double VIBE_PERIOD_SECONDS = 0.05;
    private static final double VIBE_HUE_STEP = 3.0;
    private static final int MAX_HUE = 180;
    public LEDCommands commands = new LEDCommands();

    DigitalOutput leftRed;
    DigitalOutput leftGreen;
    DigitalOutput leftBlue;
    DigitalOutput rightRed;
    DigitalOutput rightGreen;
    DigitalOutput rightBlue;
    DigitalOutput bottomRed;
    DigitalOutput bottomGreen;
    DigitalOutput bottomBlue;


    boolean vibe = true;

    double vibingHue;
    int shotCounter;
    double lastVibeTime;
    public double sliderValue = 1.0;
    boolean pwmStarted;

    public void startup() {
        if (leftRed == null) {
            leftRed = new DigitalOutput(DIO.LED_Red_Left);
            leftGreen = new DigitalOutput(DIO.LED_Green_Left);
            leftBlue = new DigitalOutput(DIO.LED_Blue_Left);
            rightRed = new DigitalOutput(DIO.LED_Red_Right);
            rightGreen = new DigitalOutput(DIO.LED_Green_Right);
            rightBlue = new DigitalOutput(DIO.LED_Blue_Right);
            bottomRed = new DigitalOutput(DIO.LED_Red_Bottom);
            bottomGreen = new DigitalOutput(DIO.LED_Green_Bottom);
            bottomBlue = new DigitalOutput(DIO.LED_Blue_Bottom);
            setColor(Color.kWhite);
        }
    }

    public void setColor(Color m_color) {
        leftRed.disablePWM();
        leftGreen.disablePWM();
        leftBlue.disablePWM();

        rightRed.disablePWM();
        rightGreen.disablePWM();
        rightBlue.disablePWM();

        bottomRed.disablePWM();
        bottomGreen.disablePWM();
        bottomBlue.disablePWM();

        leftRed.enablePWM(m_color.red);
        leftGreen.enablePWM(m_color.green);
        leftBlue.enablePWM(m_color.blue);

        rightRed.enablePWM(m_color.red);
        rightGreen.enablePWM(m_color.green);
        rightBlue.enablePWM(m_color.blue);

        bottomRed.enablePWM(m_color.red);
        bottomGreen.enablePWM(m_color.green);
        bottomBlue.enablePWM(m_color.blue);
    }

    public void off() {
        setColor(Color.kBlack);
    }

    public void toggleVibeOff() {
        vibe = false;
        off();
    }

    public void toggleVibeOn() {
        vibe = true;
    }

    public void vibing() {
        if (!vibe) {
            return;
        }

        double now = Timer.getFPGATimestamp();
        if (now - lastVibeTime < VIBE_PERIOD_SECONDS) {
            return;
        }

        lastVibeTime = now;
        double speedMultiplier = Math.max(0.0, Math.min(1.0, sliderValue));
        vibingHue = (vibingHue + VIBE_HUE_STEP * speedMultiplier) % MAX_HUE;
        setColor(Color.fromHSV((int) vibingHue, 255, 255));
    }

    public class LEDCommands {
        public Command LEDDEMO() {
            return LEDOn(Color.kRed).alongWith(Commands.waitSeconds(1))
                    .andThen(LEDOn(Color.kWhite).alongWith(Commands.waitSeconds(1)))
                    .andThen(LEDOn(Color.kBlue).alongWith(Commands.waitSeconds(1)))
                    .andThen(LEDNoMoreOn());
        }

        public Command LEDNoMoreOn() {
            return runOnce(() -> off()).ignoringDisable(true);
        }

        public Command LEDOn(Color m_Color) {
            return runOnce(() -> setColor(m_Color)).ignoringDisable(true);
        }

        public Command ToggleVibe(){
            return runOnce(()->{
                vibe = !vibe;
            });
        }
    }
}