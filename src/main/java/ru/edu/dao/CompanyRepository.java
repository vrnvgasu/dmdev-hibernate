package ru.edu.dao;

import javax.persistence.EntityManager;
import ru.edu.entity.Company;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

  public CompanyRepository(EntityManager entityManager) {
    super(Company.class, entityManager);
  }

}
