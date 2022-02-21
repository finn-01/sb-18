package vn.finn.spring.testspring;

import java.util.List;

public interface TodoRepository {
    List<Todo> findAll();

    Todo findById(int id);
}
