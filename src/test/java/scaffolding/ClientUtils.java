package scaffolding;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.BufferedSink;

import java.io.IOException;

public class ClientUtils {
	public static RequestBody largeRequestBody(StringBuffer sentData) throws IOException {
		return new RequestBody() {
			@Override
			public MediaType contentType() {
				return MediaType.parse("text/plain");
			}

			@Override
			public void writeTo(BufferedSink sink) throws IOException {
				write(sink, "Numbers\n");
				write(sink, "-------\n");
				for (int i = 2; i <= 997; i++) {
					write(sink, String.format(" * %s\n", i));
				}
			}

			private void write(BufferedSink sink, String s) throws IOException {
				sentData.append(s);
				sink.writeUtf8(s);
			}

		};
	}

	public static Request.Builder request() {
		return new Request.Builder();
	}
}
