package APInLib;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VieTTSAPI implements TTSAPInLib{
    private String datajson;
    private String apiUrl = "https://viettelgroup.ai/voice/api/tts/v1/rest/syn";
    private HttpClient httpClient;
    private HttpPost request;
    @Override
    public void build(String text) {
       datajson = String.format("{\"text\":\"%s\",",text ) +
                "\"voice\":\"hn-thanhtung\"," +
                "\"id\":\"3\"," +
                "\"without_filter\":false," +
                "\"speed\":1.0," +
                "\"tts_return_option\":2}";
        httpClient = HttpClientBuilder.create().build();
        request = new HttpPost(apiUrl);
        StringEntity body = new StringEntity(datajson, "UTF-8");
        request.addHeader("content-type", "application/json;charset=UTF-8");
        request.addHeader("token", "er-4-5OYTNaHjk860QOylvY606s69ROEqH5VtnwQqkcHE87KVVAyDCgkwBaM-jdW");
        request.getRequestLine();
        request.setEntity(body);
    }

    @Override
    public void executer() {
        ExecutorService threadpool = Executors.newCachedThreadPool();
        ListeningExecutorService service = MoreExecutors.listeningDecorator(threadpool);
        service.submit(()-> {
            try {
                HttpResponse response = httpClient.execute(request);
                System.err.println(response);
                InputStream result = response.getEntity().getContent();
                AudioInputStream audioInputStream = null;
                try {
                    audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(result));
                } catch (UnsupportedAudioFileException e) {
                    throw new RuntimeException(e);
                }
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (IOException | LineUnavailableException e) {
                e.printStackTrace();
            }
        });
    }
}
