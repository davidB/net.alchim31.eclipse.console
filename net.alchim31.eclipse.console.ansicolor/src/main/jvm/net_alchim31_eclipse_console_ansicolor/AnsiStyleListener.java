package net_alchim31_eclipse_console_ansicolor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.graphics.GlyphMetrics;
import org.eclipse.swt.widgets.Display;

public class AnsiStyleListener implements LineStyleListener {
  private static final GlyphMetrics hidden = new GlyphMetrics(0, 0, 0);

  private static void appendStyle(List<StyleRange> styles, StyleRange currentStyle, int endInLine, int lineOffset) throws Exception {
    StyleRange codeStyle = new StyleRange(currentStyle);
    codeStyle.start = lineOffset + currentStyle.start;
    codeStyle.length = currentStyle.length;
    codeStyle.metrics = hidden;
    styles.add(codeStyle);
    
    StyleRange textStyle = new StyleRange(currentStyle);
    int startInLine = (currentStyle.start + currentStyle.length);
    textStyle.start = lineOffset + startInLine;
    textStyle.length = endInLine - startInLine + 1;
    styles.add(textStyle);
  }
  
  // TODO optimize parsing and matching
  @Override
  public void lineGetStyle(LineStyleEvent event) {
    try {
      if (event.lineText.length() == 0) {
        return;
      }
  
      List<AnsiCodesReader.CodeLocation> codes = AnsiCodesReader.extractCodeLocations(event.lineText);
      if (codes.size() == 0) {
        return;
      }
      
      ArrayList<StyleRange> styles = new ArrayList<StyleRange>();
      Display display = Display.getCurrent();
      StyleRange currentStyle = new StyleRange();
      currentStyle.start = 0;
      currentStyle.length = 0;
      for(int i = 0; i < codes.size(); i++) {
        AnsiCodesReader.CodeLocation cl = codes.get(i);
        if ((currentStyle.start + currentStyle.length) != cl.start) {
          appendStyle(styles, currentStyle, cl.start - 1, event.lineOffset);      
          currentStyle.start = cl.start;
        }
        AnsiCodesReader.update(currentStyle, cl.code, display);
        currentStyle.length = cl.end - currentStyle.start; 
      }
      appendStyle(styles, currentStyle, event.lineText.length() - 1, event.lineOffset);
      event.styles = styles.toArray(new StyleRange[0]);
    } catch (Exception exc) {
      //TODO Log
      exc.printStackTrace();
    }
  }
}