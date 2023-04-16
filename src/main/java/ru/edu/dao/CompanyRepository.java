package ru.edu.dao;

import org.hibernate.SessionFactory;
import ru.edu.entity.Company;

public class CompanyRepository extends RepositoryBase<Integer, Company> {

  public CompanyRepository(SessionFactory sessionFactory) {
    super(Company.class, sessionFactory);
  }

}
