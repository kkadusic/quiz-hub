package com.quizhub.property.services;

import com.quizhub.property.dto.Person;
import com.quizhub.property.dto.Quiz;
import com.quizhub.property.event.EventRequest;
import com.quizhub.property.exceptions.BadRequestException;
import com.quizhub.property.exceptions.ConflictException;
import com.quizhub.property.exceptions.InternalErrorException;
import com.quizhub.property.model.Favorite;
import com.quizhub.property.repositories.FavoriteRepository;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import static com.quizhub.property.services.PropertyService.registerEvent;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final RestTemplate restTemplate;

    public FavoriteService(FavoriteRepository favoriteRepository, RestTemplate restTemplate) {
        this.favoriteRepository = favoriteRepository;
        this.restTemplate = restTemplate;
    }

    public Iterable<Favorite> getAllFavorites() {
        registerEvent(EventRequest.actionType.GET, "/api/favorites/all", "200");
        return favoriteRepository.findAll();
    }

    public Favorite getFavoriteById(UUID id) {
        registerEvent(EventRequest.actionType.GET, "/api/favorites", "200");
        return favoriteRepository.findById(id)
                .orElseThrow(() -> {
                    registerEvent(EventRequest.actionType.GET, "/api/favorite", "400");
                    return new BadRequestException("Favorite with id " + id + " does not exist");
                });
    }

    public List<Quiz> getAllFavoritesByUser(String username) {
        Person person;
        try {
            person = restTemplate.getForObject("http://person-service/api/persons/username?username=" + username, Person.class);
        } catch (Exception e) {
            throw new BadRequestException("Quiz or person does not exist");
        }
        registerEvent(EventRequest.actionType.GET, "/api/favorites/all/user", "200");

        List<Favorite> favorites = favoriteRepository.getFavoriteByPerson(person.getId());

        List<Quiz> quizzes = new ArrayList<>();
        for (Favorite favorite : favorites) {
            try {
                Quiz quiz = restTemplate.getForObject("http://quiz-service/api/quizzes?id=" + favorite.getQuiz(), Quiz.class);
                quizzes.add(quiz);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException("Quiz or person does not exist");
            }
        }

        return quizzes;
    }

    public Iterable<Favorite> getAllFavoritesByQuiz(UUID id) {
        registerEvent(EventRequest.actionType.GET, "/api/favorites/all/quiz", "200");
        return favoriteRepository.getFavoriteByQuiz(id)
                .orElseThrow(() -> {
                    registerEvent(EventRequest.actionType.GET, "/api/favorites/all/quiz", "400");
                    return new BadRequestException("Quiz with id " + id.toString() + " does not exist");
                });
    }

    public Favorite addFavorite(Favorite favorite) {
        try {
            Quiz quiz;
            Person person;
            if (favorite.getPerson() == null || favorite.getQuiz() == null) {
                throw new BadRequestException("Quiz or person cannot be null");
            }
            try {
                person = restTemplate.getForObject("http://person-service/api/persons?id=" + favorite.getPerson(), Person.class);
                quiz = restTemplate.getForObject("http://quiz-service/api/quizzes?id=" + favorite.getQuiz(), Quiz.class);
            } catch (Exception e) {
                e.printStackTrace();
                throw new BadRequestException("Quiz or person does not exist");
            }
            if (favoriteRepository.existsByQuizAndPerson(favorite.getQuiz(), favorite.getPerson())) {
                throw new ConflictException("Favorite already exists");
            }
            registerEvent(EventRequest.actionType.CREATE, "/api/favorites", "200");
            return favoriteRepository.save(favorite);
        } catch (ConflictException exception) {
            registerEvent(EventRequest.actionType.CREATE, "/api/favorites", "409");
            throw exception;
        } catch (BadRequestException exception) {
            registerEvent(EventRequest.actionType.CREATE, "/api/favorites", "400");
            throw exception;
        }
    }

    @Transactional
    public JSONObject deleteFavorite(UUID id) {
        try {
            if (!favoriteRepository.existsById(id)) {
                throw new BadRequestException("Favorite with id " + id + " does not exist");
            }
            favoriteRepository.deleteById(id);
            if (favoriteRepository.existsById(id)) {
                throw new InternalErrorException("Favorite was not deleted (database issue)");
            }
            registerEvent(EventRequest.actionType.DELETE, "/api/favorites", "200");
            return new JSONObject(new HashMap<String, String>() {{
                put("message", "Favorite with id " + id.toString() + " has been successfully deleted");
            }});
        } catch (BadRequestException exception) {
            registerEvent(EventRequest.actionType.DELETE, "/api/favorites", "400");
            throw exception;
        }
    }
}
