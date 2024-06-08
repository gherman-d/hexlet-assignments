package exercise.controller.users;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

import exercise.model.Post;
import exercise.Data;

// BEGIN
@RestController
@RequestMapping("/api")
public class PostsController {
  // Хранилище добавленных постов
  private List<Post> posts = Data.getPosts();

    //GET /api/users/{id}/posts — список всех постов, написанных пользователем с таким же userId, как id в маршруте
    @GetMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.OK)
    public List<Post> index(@PathVariable String id) {
        var result = posts.stream()
                    .filter(p -> String.valueOf(p.getUserId()).equals(id))
                    .toList();
        return result;
    }

    //POST /api/users/{id}/posts – создание нового поста, привязанного к пользователю по id.
    //Код должен возвращать статус 201, тело запроса должно содержать slug, title и body.
    //Обратите внимание, что userId берется из маршрута
    @PostMapping("/users/{id}/posts")
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@PathVariable String id, @RequestBody Post data) {
        Post newPost = new Post();

        newPost.setUserId(Integer.parseInt(id));
        newPost.setSlug(data.getSlug());
        newPost.setTitle(data.getTitle());
        newPost.setBody(data.getBody());

        posts.add(newPost);
        return newPost;
    }
}
// END
