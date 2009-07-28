package org.basex.core.proc;

import static org.basex.Text.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.basex.BaseX;
import org.basex.core.Context;
import org.basex.core.Process;
import org.basex.data.Data;
import org.basex.data.DiskData;
import org.basex.io.IO;

/**
 * Opens an existing database.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 */
public final class Open extends Process {
  /**
   * Constructor.
   * @param name name of database
   */
  public Open(final String name) {
    super(STANDARD, name);
  }

  @Override
  protected boolean exec() {
    exec(new Close());
    try {
      final Data data = open(context, args[0]);
      context.openDB(data);

      if(data.meta.oldindex) info(INDUPDATE);
      return info(DBOPENED, perf.getTimer());
    } catch(final IOException ex) {
      BaseX.debug(ex);
      final String msg = ex.getMessage();
      return error(msg.length() != 0 ? msg : DBOPENERR);
    }
  }

  /**
   * Opens the specified database.
   * @param ctx database context
   * @param db name of database
   * @return data reference
   * @throws IOException I/O exception
   */
  public static Data open(final Context ctx, final String db)
      throws IOException {

    // check if document exists
    if(!ctx.prop.dbpath(db).exists())
      throw new FileNotFoundException(BaseX.info(DBNOTFOUND, db));

    Data data = ctx.pin(db);
    if(data == null) {
      data = new DiskData(db, ctx.prop);
      ctx.addToPool(data);
    }
    return data;
  }

  /**
   * Opens the specified database; if it does not exist, create a new
   * database instance.
   * @param ctx database context
   * @param path document path
   * @return data reference
   * @throws IOException I/O exception
   */
  public static Data check(final Context ctx, final String path)
      throws IOException {

    final IO f = IO.get(path);
    final String db = f.dbname();
    return ctx.prop.dbpath(db).exists() ?
        open(ctx, db) : CreateDB.xml(ctx, f, db);
  }
}
