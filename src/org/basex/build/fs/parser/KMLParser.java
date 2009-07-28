package org.basex.build.fs.parser;

import java.io.IOException;

import org.basex.BaseX;
import org.basex.build.fs.NewFSParser;
import org.basex.build.fs.parser.Metadata.MetaType;
import org.basex.build.fs.parser.Metadata.MimeType;

/**
 * Parser for KML files.
 *
 * @author Workgroup DBIS, University of Konstanz 2005-09, ISC License
 * @author Bastian Lemke
 */
public final class KMLParser extends AbstractParser {

  static {
    NewFSParser.register("kml", KMLParser.class);
  }

  /** Standard constructor. */
  public KMLParser() {
    super(MetaType.XML, MimeType.KML.get());
  }

  @Override
  public boolean check(final BufferedFileChannel f) {
    return true;
  }

  @Override
  public void readContent(final BufferedFileChannel bfc,
      final NewFSParser parser) throws IOException {

    if(bfc.isSubChannel()) BaseX.notimplemented("Parsing framents of kml files "
        + "is currently not supported");
    parser.parseXML();
  }

  @Override
  public void meta(final BufferedFileChannel bfc, final NewFSParser parser) {
  // no metadata to read...
  }
}
