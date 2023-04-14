package ru.edu.listener;

import org.hibernate.envers.RevisionListener;
import ru.edu.entity.Revision;

public class MyRevisionListener implements RevisionListener {

  @Override
  public void newRevision(Object o) {
    ((Revision) o).setUsername("current user");
  }

}
