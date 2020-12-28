package com.example.afip.gringa;

import com.example.afip.gringa.jobs.AfipJob;
import com.example.afip.gringa.service.EmailService;
import com.example.afip.gringa.service.PrinterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

@SpringBootTest
@PropertySource("classpath:application.properties")
@PropertySource("classpath:local-sensitive.conf")
class GringaApplicationTests {

	@Autowired
	private EmailService emailService;

	@Autowired
	private AfipJob afipJob;

	@Autowired
	private PrinterService printService;

	@Test
	void contextLoads() throws InterruptedException {
		afipJob.execute();
	}

}
