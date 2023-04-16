package ru.edu.dao;

import org.hibernate.SessionFactory;
import ru.edu.entity.Payment;

public class PaymentRepository extends RepositoryBase<Long, Payment> {

  public PaymentRepository(SessionFactory sessionFactory) {
    super(Payment.class, sessionFactory);
  }

}
