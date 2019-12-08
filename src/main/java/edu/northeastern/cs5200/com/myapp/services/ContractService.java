package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.ContractStatus;
import edu.northeastern.cs5200.com.myapp.models.Event;
import edu.northeastern.cs5200.com.myapp.repositories.ContractRepository;
import edu.northeastern.cs5200.com.myapp.repositories.EventRepository;

@Service
public class ContractService {

  @Autowired
  private ContractRepository contractRepository;

  @Autowired
  private EventRepository eventRepository;

  public Contract findContractById(int contractId) {
    return contractRepository.findById(contractId).get();
  }

  public Contract acceptContract(int contractId) {
    Contract contract = contractRepository.findById(contractId).get();
    contract.setStatus(ContractStatus.ACCEPTED.getStatus());
    return contractRepository.save(contract);

  }

  public Contract rejectContract(int contractId) {
    Contract contract = contractRepository.findById(contractId).get();
    contract.setStatus(ContractStatus.REJECTED.getStatus());
    return contractRepository.save(contract);

  }

  public Event createEvent(String name, String description, int capacity, String date, Contract contract) {
    Event newEvent = new Event(name, description, capacity, date, contract);
    return eventRepository.save(newEvent);
  }

  public Contract save(Contract requested) {
    return contractRepository.save(requested);
  }
}
