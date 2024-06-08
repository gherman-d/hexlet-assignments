package exercise;

import java.net.URI;
import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import exercise.model.Post;

@SpringBootApplication
@RestController
public class Application {
    // Хранилище добавленных постов
    private List<Post> posts = Data.getPosts();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    // BEGIN
    @GetMapping("/posts") // список всех постов. Должен возвращаться статус 200 и заголовок X-Total-Count, в котором содержится количество постов
        public ResponseEntity<List<Post>> index(@RequestParam(defaultValue = "10") Integer limit) {
        var result = posts.stream().limit(limit).toList();

        return ResponseEntity.ok()
                .header("X-Total-Count", String.valueOf(posts.size()))
                .body(result);
    }

    @GetMapping("/posts/{id}") // просмотр конкретного поста. Если пост найден, должен возвращаться статус 200, если нет — статус 404
    public ResponseEntity<Post> show(@PathVariable String id) {
        var post = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        return ResponseEntity.of(post);
    }

   @PostMapping("/posts") // создание нового поста. Должен возвращаться статус 201
   public ResponseEntity<Post> create(@RequestBody Post post) {
        posts.add(post);
            return ResponseEntity.status(HttpStatus.CREATED).body(post);
   }

    @PutMapping("/posts/{id}") // Обновление поста. Должен возвращаться статус 200. Если пост уже не существует, то должен возвращаться 204
    public ResponseEntity<Post> update(@PathVariable String id, @RequestBody Post data) {
        var currentpage = posts.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst();
        if (currentpage.isPresent()) {
            var page = currentpage.get();
            page.setId(data.getId());
            page.setTitle(data.getTitle());
            page.setBody(data.getBody());

            return ResponseEntity.status(HttpStatus.OK).body(data);
        }
        else {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(data);
        }
    }
    // END

    @DeleteMapping("/posts/{id}")
    public void destroy(@PathVariable String id) {
        posts.removeIf(p -> p.getId().equals(id));
    }
}
