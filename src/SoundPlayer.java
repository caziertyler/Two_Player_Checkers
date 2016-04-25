package CheckersClient;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Tyler on 4/18/2016.
 */
public class SoundPlayer {
    public static final String SOUND_PATH_CHAT_RECEIVE = "res\\SoundChatReceive3.wav";
    public static final String SOUND_PATH_CHAT_SEND = "res\\SoundChatSend1.wav";
    public static final String SOUND_PATH_CHECKER_JUMP = "res\\SoundCheckerJump.wav";
    public static final String SOUND_PATH_CHECKER_SELECT = "res\\SoundCheckerSelect2.wav";
    public static final String SOUND_PATH_CHECKER_PLACE = "res\\SoundCheckerPlace.wav";
    public static final String SOUND_PATH_GAME_START = "res\\SoundGameStart.wav";
    public static final String SOUND_PATH_GAME_WIN = "res\\SoundGameWin2.wav";
    public static final String SOUND_PATH_GAME_LOSE = "res\\SoundGameLose1.wav";

    /**
     * Plays an audio file at the location provided for the given length.
     * @param location - A string for the file's url.
     */
    public static void playSound (String location) {
        InputStream in = null;

        try {
            in = new FileInputStream(location);

        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            System.out.println("Media file not present in C drive.");

            e.printStackTrace();
        }

        AudioStream as = null;

        try {
            as = new AudioStream(in);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        AudioPlayer.player.start(as);
    }
}
