package com.aftarobot.wallet.data.payments;

public class Links
{
  private Self self;

  public Self getSelf() { return this.self; }

  public void setSelf(Self self) { this.self = self; }

  private Next next;

  public Next getNext() { return this.next; }

  public void setNext(Next next) { this.next = next; }

  private Prev prev;

  public Prev getPrev() { return this.prev; }

  public void setPrev(Prev prev) { this.prev = prev; }
}