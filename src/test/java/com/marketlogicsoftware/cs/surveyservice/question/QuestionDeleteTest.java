package com.marketlogicsoftware.cs.surveyservice.question;

import com.marketlogicsoftware.cs.surveyservice.survey.models.AnswerType;
import com.marketlogicsoftware.cs.surveyservice.survey.models.Question;
import com.marketlogicsoftware.cs.surveyservice.survey.models.Survey;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionDeleteTest {

    @Autowired
    private WebTestClient webTestClient;

    @LocalServerPort
    private int randomPort;

    private List<URI> questionURIs;
    private RestTemplate restTemplate = new RestTemplate();

    private Collection<Question> questions() {
        return Arrays.asList(
                new Question("Is Java your favorite language", "Basics", AnswerType.Single)
        );
    }

    private void testDeleteQuestion(URI questionURI) {
        webTestClient.delete()
                     .uri(questionURI)
                     .exchange()
                     .expectStatus()
                     .isOk();
    }

    @Before
    public void setup() throws URISyntaxException {
        questionURIs = new ArrayList<>();

        URI surveyURI = restTemplate.postForLocation(
                new URI("http://localhost:" + randomPort + "/api/survey")
                , new Survey("Java", "Java Survey"));

        for (Question question : questions()) {
            questionURIs.add(
                    restTemplate.postForLocation(surveyURI + "/question", question)
            );
        }
    }

    @Test
    public void testDeleteQuestion() {
        for (URI questionURI : questionURIs) {
            testDeleteQuestion(questionURI);
        }
    }
    
}