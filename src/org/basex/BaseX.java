package org.basex;

import static org.basex.core.Text.*;
import java.io.IOException;
import org.basex.core.Session;
import org.basex.core.LocalSession;
import org.basex.core.Main;
import org.basex.core.Prop;
import org.basex.core.proc.CreateDB;
import org.basex.core.proc.XQuery;
import org.basex.io.IO;
import org.basex.util.Args;
import org.basex.util.Token;

/**
 * This is the starter class for the stand-alone console mode.
 * It executes all commands locally.
 * Next, the class offers some utility methods which are used
 * throughout the project.
 * Add the '-h' option to get a list on all available command-line arguments.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Christian Gruen
 */
public class BaseX extends Main {
  /** User query. */
  private String commands;
  /** XQuery file. */
  private String file;
  /** User name. */
  protected String user;

  /**
   * Main method, launching the standalone console mode.
   * Use <code>-h</code> to get a list of optional command-line arguments.
   * @param args command-line arguments
   */
  public static void main(final String... args) {
    new BaseX(args);
  }

  /**
   * Constructor.
   * @param args command line arguments
   */
  protected BaseX(final String... args) {
    super(args);
    if(success) run();
  }

  /**
   * Constructor.
   */
  protected final void run() {
    try {
      boolean u = false;
      if(input != null) process(new CreateDB(input), true);
      
      if(file != null) {
        // query file contents
        Prop.xquery = IO.get(file);
        final String query = content();
        if(query != null) process(new XQuery(query), true);
      } else if(commands != null) {
        // process command-line arguments
        process(commands);
      } else {
        // enter interactive mode
        outln(CONSOLE, sa() ? LOCALMODE : CLIENTMODE, CONSOLE2);
        u = console();
      }
      quit(u);
    } catch(final IOException ex) {
      error(ex, true);
    }
  }

  /**
   * Reads in a query file and returns the content.
   * @return file content
   */
  private String content() {
    final IO io = IO.get(file);
    if(!io.exists()) {
      errln(FILEWHICH, file);
    } else {
      try {
        return Token.string(io.content()).trim();
      } catch(final IOException ex) {
        error(ex, ex.getMessage());
      }
    }
    return null;
  }

  /**
   * Returns if this is the standalone version.
   * @return standalone flag
   */
  protected boolean sa() {
    return true;
  }

  @Override
  @SuppressWarnings("unused")
  protected Session session() throws IOException {
    if(session == null) session = new LocalSession(context);
    return session;
  }

  @Override
  protected final void parseArguments(final String[] args) {
    try {
      final Args arg = new Args(args);
      success = true;
      while(arg.more() && success) {
        if(arg.dash()) {
          final char c = arg.next();
          if(c == 'c') {
            // send database commands
            commands = arg.remaining();
          } else if(c == 'd') {
            // activate debug mode
            context.prop.set(Prop.DEBUG, true);
          } else if(c == 'D' && sa()) {
            // hidden option: show dot query graph
            success = set(Prop.DOTPLAN, true);
          } else if(c == 'i' && sa()) {
            // hidden option: show dot query graph
            input = arg.string();
          } else if(c == 'm') {
            // hidden option: activate table main memory mode
            success = set(Prop.TABLEMEM, true);
          } else if(c == 'M') {
            // hidden option: activate main memory mode
            success = set(Prop.MAINMEM, true);
          } else if(c == 'n' && !sa()) {
            // parse server name
            context.prop.set(Prop.HOST, arg.string());
          } else if(c == 'o') {
            // specify file for result output
            output = arg.string();
          } else if(c == 'p' && !sa()) {
            // parse server port
            context.prop.set(Prop.PORT, arg.num());
          } else if(c == 'r') {
            // hidden option: parse number of runs
            success = set(Prop.RUNS, arg.string());
          } else if(c == 'u' && !sa()) {
            // specify user name
            user = arg.string();
          } else if(c == 'v') {
            // show process info
            success = set(Prop.INFO, true);
          } else if(c == 'V') {
            // show all process info
            success = set(Prop.INFO, ALL);
          } else if(c == 'x') {
            // activate well-formed XML output
            success = set(Prop.XMLOUTPUT, true);
          } else if(c == 'X') {
            // hidden option: show xml query plan
            success = set(Prop.XMLPLAN, true);
          } else if(c == 'z') {
            // turn off result serialization
            success = set(Prop.SERIALIZE, false);
          } else {
            success = false;
          }
        } else {
          file = file == null ? arg.string() : file + " " + arg.string();
        }
      }
      console = file == null && commands == null;
      if(!success) outln(sa() ? LOCALINFO : CLIENTINFO);
    } catch(final IOException ex) {
      error(ex, true);
      success = false;
    }
  }
}
