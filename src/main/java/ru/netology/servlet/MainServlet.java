package ru.netology.servlet;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ru.netology.controller.PostController;
import ru.netology.repository.PostRepository;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MainServlet extends HttpServlet {
  private PostController controller;
  private static final String get = "GET";
  private static final String post = "POST";
  private static final String delete = "DELETE";
  private static final String apiPath = "/api/posts";



  @Override
  public void init() {
 //   final var repository = new PostRepository();
 //   final var service = new PostService(repository);
 //   controller = new PostController(service);

    final var context = new AnnotationConfigApplicationContext("ru.netology");
    // получаем по имени бина
    final var controller = context.getBean("postController");
  }

  @Override
  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    // если деплоились в root context, то достаточно этого
    try {
      final var path = req.getRequestURI();
      final var method = req.getMethod();
      // primitive routing
      if (method.equals(get) && path.equals(apiPath)) {
        controller.all(resp);
        return;
      }
      if (method.equals(get) && path.matches(apiPath + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(1 + path.lastIndexOf("/")));
        controller.getById(id, resp);
        return;
      }
      if (method.equals(post) && path.equals(apiPath)) {
        controller.save(req.getReader(), resp);
        return;
      }
      if (method.equals(delete) && path.matches(apiPath + "/\\d+")) {
        // easy way
        final var id = Long.parseLong(path.substring(1 + path.lastIndexOf("/")));
        controller.removeById(id, resp);

        return;
      }
      resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } catch (Exception e) {
      e.printStackTrace();
      resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
  }
}

