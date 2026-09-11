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

    DigitalOutput RL;
    DigitalOutput GL;
    DigitalOutput BL;
    DigitalOutput RR;
    DigitalOutput GR;
    DigitalOutput BR;

    boolean vibe = true;

    double vibingHue;
    int shotCounter;
    double lastVibeTime;
    public double sliderValue = 1.0;
    boolean pwmStarted;

    public void startup() {
        if (RL == null) {
            RL = new DigitalOutput(DIO.LED_Red_L);
            GL = new DigitalOutput(DIO.LED_Green_L);
            BL = new DigitalOutput(DIO.LED_Blue_L);
            RR = new DigitalOutput(DIO.LED_Red_R);
            GR = new DigitalOutput(DIO.LED_Green_R);
            BR = new DigitalOutput(DIO.LED_Blue_R);
            setColor(Color.kBlack);
        }
    }

    public void setColor(Color m_color) {
        if (!pwmStarted) {
            RL.enablePWM(m_color.red);
            GL.enablePWM(m_color.green);
            BL.enablePWM(m_color.blue);

            RR.enablePWM(m_color.red);
            GR.enablePWM(m_color.green);
            BR.enablePWM(m_color.blue);
            pwmStarted = true;
            return;
        }

        RL.updateDutyCycle(m_color.red);
        GL.updateDutyCycle(m_color.green);
        BL.updateDutyCycle(m_color.blue);

        RR.updateDutyCycle(m_color.red);
        GR.updateDutyCycle(m_color.green);
        BR.updateDutyCycle(m_color.blue);
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
