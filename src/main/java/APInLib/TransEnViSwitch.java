package APInLib;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransEnViSwitch implements TransAPI {
    private String urlStr;
    public void build(String text, String langTo, String langFrom)  {
        try {
            urlStr = "https://script.google.com/macros/s/AKfycbzhA3frpIsUaSJaamXOFu84mtT194zjgvllttWHBij2et_ixDAlK8TIVvIFw0sjIcgePA/exec" +
                    "?q=" + URLEncoder.encode(text, "UTF-8") +
                    "&target=" + langTo +
                    "&source=" + langFrom;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
    public String executer(){
        try {
            URL url = new URL(urlStr);
            StringBuilder response = new StringBuilder();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
