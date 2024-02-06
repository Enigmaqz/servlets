package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.*;

// Stub
public class PostRepository {

  private ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
  private AtomicLong counterPosts = new AtomicLong(1);

  public List<Post> all() {
    //return Collections.emptyList();
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    //return Optional.empty();
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      post.setId(counterPosts.getAndIncrement());
      posts.put(post.getId(), post);
    } else if (posts.containsKey(post.getId())) {
      posts.put(post.getId(), post);
    } else {
      throw new NotFoundException();
    }
    return post;
  }

  public void removeById(long id) {
    if (posts.containsKey(id)) {
      posts.remove(id);
    } else {
      throw new NotFoundException();
    }
  }
}
