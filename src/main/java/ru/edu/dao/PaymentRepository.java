package ru.edu.dao;

import javax.persistence.EntityManager;
import ru.edu.entity.Payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

  public PaymentRepository(EntityManager entityManager) {
    super(Payment.class, entityManager);
  }

}
