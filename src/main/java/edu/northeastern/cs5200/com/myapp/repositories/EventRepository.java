package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.repository.CrudRepository;

import edu.northeastern.cs5200.com.myapp.models.Event;

public interface EventRepository extends CrudRepository<Event, Integer> {
}
