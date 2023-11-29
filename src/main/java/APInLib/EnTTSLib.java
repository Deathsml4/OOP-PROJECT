package APInLib;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.voicerss.tts.*;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EnTTSLib implements TTSAPInLib {
    private VoiceProvider tts = new VoiceProvider("78c77fb09be74d449cf7e451083b3621");
    private VoiceParameters params;
    public void build(String text){
        params = new VoiceParameters(text, Languages.English_UnitedStates);
        params.setCodec(AudioCodec.WAV);
        params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        params.setBase64(false);
        params.setSSML(false);
        params.setRate(0);
    }
    @Override
    public void executer() {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        service.submit(()-> {
        try{
            byte[] voice = tts.speech(params);
            FileOutputStream fos = new FileOutputStream("voice.mp3");
            fos.write(voice, 0, voice.length);
            fos.flush();
            fos.close();
        } catch(Exception e){
            System.out.println("TTS Error");
        }
        try{
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File("voice.mp3").getAbsoluteFile());
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        }catch(Exception e){
            System.out.println("TTS Error");
        }
        });
    }
}
