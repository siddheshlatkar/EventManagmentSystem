package edu.northeastern.cs5200.com.myapp.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.northeastern.cs5200.com.myapp.models.Contract;

@Repository
public interface ContractRepository extends CrudRepository<Contract, Integer> {
}
