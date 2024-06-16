package PaooGame;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/music.wav");
        soundURL[1] = getClass().getResource("/sound/unlock.wav");
        soundURL[2] = getClass().getResource("/sound/receivedamage.wav");
        soundURL[3] = getClass().getResource("/sound/hitmonster.wav");
        soundURL[4] = getClass().getResource("/sound/parry.wav");
    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        }catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void play() {
        clip.start();
    }
    public void loop() {
        clip.loop(clip.LOOP_CONTINUOUSLY);
    }
    public void stop() {
        clip.stop();
    }
}
