package com.sport.mock;

import com.sport.mock.model.UserProfile;
import com.sport.mock.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.logging.Logger;

@SpringBootApplication
public class MockApplication implements CommandLineRunner {


	public static void main(String[] args) {
		SpringApplication.run(MockApplication.class, args);
	}


	@Autowired
	private UserProfileRepository userProfileRepository;


	@Override
	public void run(String... args) throws Exception {

		UserExerciceThread userExerciceThread = null;

		List<UserProfile> userProfileList = (List<UserProfile>) userProfileRepository.findAll();

		for(UserProfile userProfile: userProfileList){
			userExerciceThread = new UserExerciceThread(userProfile);
			userExerciceThread.start();
		}
	}
}
