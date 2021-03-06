package org.basex.query.util.list;

import org.basex.query.*;
import org.basex.query.expr.*;
import org.basex.query.value.*;
import org.basex.query.value.item.*;
import org.basex.query.value.seq.*;
import org.basex.query.value.type.*;
import org.basex.util.list.*;

/**
 * Resizable-array implementation for items.
 *
 * @author BaseX Team 2005-19, BSD License
 * @author Leo Woerteler
 */
public final class ItemList extends ObjectList<Item, ItemList> {
  /**
   * Default constructor.
   */
  public ItemList() {
    this(1);
  }

  /**
   * Constructor, specifying an initial array capacity.
   * @param capacity array capacity
   */
  public ItemList(final int capacity) {
    super(new Item[capacity]);
  }

  /**
   * Constructor, specifying an initial array capacity.
   * @param capacity array capacity (can be negative)
   * @throws QueryException query exception
   */
  public ItemList(final long capacity) throws QueryException {
    this(ValueList.capacity(capacity));
  }

  /**
   * Adds all items of a value to the array.
   * @param value value to be added
   * @return self reference
   */
  public ItemList add(final Value value) {
    for(final Item item : value) add(item);
    return this;
  }

  /**
   * Returns a value containing the items in this list.
   * @return the value
   */
  public Value value() {
    return value((Type) null);
  }

  /**
   * Returns a value with the type of the given expression.
   * @param type type (can be {@code null}, only considered if new sequence is created)
   * @return the value
   */
  public Value value(final Type type) {
    return ItemSeq.get(list, size, type);
  }

  /**
   * Returns a value with the type of the given expression.
   * @param expr expression (can be {@code null})
   * @return the value
   */
  public Value value(final Expr expr) {
    return ItemSeq.get(list, size, expr);
  }

  @Override
  protected Item[] newList(final int s) {
    return new Item[s];
  }
}
