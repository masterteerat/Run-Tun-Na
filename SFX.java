import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SFX {
    private Clip clip;
    private FloatControl volumeControl;

    public SFX(String filePath) {
        try {
            File soundFile = new File(filePath);
            if (!soundFile.exists()) {
                System.out.println("Error: Sound file not found -> " + filePath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            } else {
                System.out.println("Warning: Volume control not supported");
            }

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void setVolume(float volume) {
        if (volumeControl != null) {
            volume = Math.max(volumeControl.getMinimum(), Math.min(volume, volumeControl.getMaximum()));
            volumeControl.setValue(volume);
        } else {
            System.out.println("Warning: Volume control not supported");
        }
    }
}
