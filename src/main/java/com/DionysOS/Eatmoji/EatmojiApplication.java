package com.DionysOS.Eatmoji;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EatmojiApplication {

	public static void main(String[] args) {

		// .env 로드
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()
				.load();

		// .env 값들을 System 환경변수로 등록
		System.setProperty("AWS_ACCESS_KEY_ID", dotenv.get("AWS_ACCESS_KEY_ID"));
		System.setProperty("AWS_SECRET_ACCESS_KEY", dotenv.get("AWS_SECRET_ACCESS_KEY"));
		System.setProperty("REGION", dotenv.get("REGION"));
		System.setProperty("S3_BUCKET_NAME", dotenv.get("S3_BUCKET_NAME"));

		SpringApplication.run(EatmojiApplication.class, args);
	}

}
