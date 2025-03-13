import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class BGM {
    private Clip clip;
    private boolean isPlaying = false;
    private FloatControl volumeControl;

    public BGM(String filePath) {
        try {
            File musicFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            clip = AudioSystem.getClip();
            clip.open(audioStream);

            volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            setVolume(-10.0f);

        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null && !isPlaying) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            clip.start();
            isPlaying = true;
        }
    }

    public void stop() {
        if (clip != null && isPlaying) {
            clip.stop();
            clip.flush();
            isPlaying = false;
        }
    }
    public void setVolume(float volume) {
        if (volumeControl != null) {
            volume = Math.max(volumeControl.getMinimum(), Math.min(volume, volumeControl.getMaximum())); // จำกัดช่วงเสียง
            volumeControl.setValue(volume);
        }
    }
}
