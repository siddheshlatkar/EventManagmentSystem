package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.northeastern.cs5200.com.myapp.models.Artist;
import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.ContractStatus;
import edu.northeastern.cs5200.com.myapp.models.Manager;
import edu.northeastern.cs5200.com.myapp.repositories.ArtistRepository;
import edu.northeastern.cs5200.com.myapp.repositories.ContractRepository;
import edu.northeastern.cs5200.com.myapp.repositories.ManagerRepository;

@Service
public class ManagerService {

  @Autowired
  private ManagerRepository managerRepository;

  @Autowired
  private ArtistRepository artistRepository;

  @Autowired
  private ContractRepository contractRepository;

  public Manager registerNewManager(Manager artist) {
    Manager alreadyRegisteredManager = managerRepository.findByUserName(artist.getUserName());
    if (alreadyRegisteredManager != null) {
      return null;
    }
    return managerRepository.save(artist);
  }

  public Manager findByUserIDAndPassword(String userName, String password) {
    return managerRepository.findByUserNameAndPassword(userName, password);
  }

  public Manager findManagerByID(int id) {
    return managerRepository.findById(id).get();
  }

  public List<Manager> findAllManagers() {
    Iterable<Manager> it =  managerRepository.findAll();
    List<Manager> managers = new ArrayList();

    for (Manager manager : it) {
      managers.add(manager);
    }
    return managers;
  }

  public Contract createContract(Manager manager, Artist artist, String text) {
    Contract contract = new Contract(manager, artist, Calendar.getInstance().getTime().toString(), text, ContractStatus.REQUESTED.getStatus());
    manager.addContract(contract);
    artist.addContract(contract);
    contractRepository.save(contract);
    return contract;
  }

  public List<Contract> getContracts(Integer id) {
    Manager manager = managerRepository.findById(id).get();
    return manager.getContracts();

  }
}
