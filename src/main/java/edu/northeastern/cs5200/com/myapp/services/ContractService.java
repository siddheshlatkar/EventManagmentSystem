package edu.northeastern.cs5200.com.myapp.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.northeastern.cs5200.com.myapp.models.Contract;
import edu.northeastern.cs5200.com.myapp.models.ContractStatus;
import edu.northeastern.cs5200.com.myapp.repositories.ContractRepository;

@Service
public class ContractService {

  @Autowired
  private ContractRepository contractRepository;

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
}
