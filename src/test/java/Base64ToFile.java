import java.io.*;
import java.util.Base64;

public class Base64ToFile {
    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new FileReader("C:\\Users\\zw\\base64.css"));
        String s;
        while ((s = bufferedReader.readLine()) != null) {
            File f = new File("C:\\Users\\zw\\base64.png");
            FileOutputStream bufferedWriter = new FileOutputStream(f);

            bufferedWriter.write(Base64.getDecoder().decode(s));
            bufferedWriter.flush();
            bufferedWriter.close();
        }
    }
}
