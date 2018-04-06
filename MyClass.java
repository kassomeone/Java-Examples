import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.json.JSONArray;

public class MyClass {

	static JSONArray obj = new JSONArray();

	public static void main(String[] args) throws InterruptedException {

		ExecutorService service = Executors.newFixedThreadPool(10);

		for (int i = 1; i < 101; i++) {
//			new Thread(new HttpCallerThread(String.valueOf(i),obj)).start();
			Runnable worker = new HttpCallerThread(String.valueOf(i), obj);
			service.execute(worker);
			
		}
		service.shutdown();
		while (!service.isTerminated()) {
		}

		System.out.println(obj.length());
		System.out.println("Finished all threads");
	}
}

class HttpCallerThread implements Runnable {

	String postId;
	JSONArray list;

	public HttpCallerThread(String id, JSONArray list) {
		this.postId = id;
		this.list = list;
	}

	@Override
	public void run() {
		try {
			URL url = new URL("https://jsonplaceholder.typicode.com/posts/" + postId);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			BufferedReader line = new BufferedReader(new InputStreamReader(con.getInputStream()));
			StringBuffer s = new StringBuffer();
			String l = "";
			while ((l = line.readLine()) != null) {
				s.append(l);
			}
			list.put(s);

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}