package net_alchim31_eclipse_console_ansicolor;

import java.util.List;

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;

public class AnsiStyleListener implements LineStyleListener {

  
  // TODO optimize parsing and matching
  @Override
  public void lineGetStyle(LineStyleEvent event) {
    try {
      if (event.lineText.length() == 0) {
        return;
      }
      List<StyleRange> styles =  AnsiCodesReader.extractStyleRange(event.lineText, event.lineOffset, Display.getCurrent());
      event.styles = styles.toArray(new StyleRange[0]);
    } catch (Exception exc) {
      //TODO Log
      exc.printStackTrace();
    }
  }
}