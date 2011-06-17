package net_alchim31_eclipse_console_ansicolor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.swt.widgets.Display;

/**
 * @see http://en.wikipedia.org/wiki/ANSI_escape_code
 * 
 * @TODO support xterm-256 text color (see http://www.eclipse.org/articles/Article-SWT-Color-Model/swt-color-model.htm)
 * @TODO test
 */
class AnsiCodesReader {
  
  //---------------------------------------------------------------------------
  //-- parsing

  private static final String CSI1 = "\0x9B";
  private static final String CSI2 = "\033\\[";
  
  private static final Pattern finder = Pattern.compile("("+ CSI1 + "|" + CSI2 +")(\\d*)(;(\\d+))?m");
  
  public static class CodeLocation {
    int code = -1;
    int start = 0;
    int end = 0;
    
    public CodeLocation(int code, int start, int end) {
      super();
      this.code = code;
      this.start = start;
      this.end = end;
    }
  }
  
  //@TODO optimize parsing
  /**
   * "CSI n [;k] m" Sets SGR parameters, including text color.
   * After CSI can be zero or more parameters separated with ;. With no parameters,
   * CSI m is treated as CSI 0 m (reset / normal), which is typical of most of the ANSI escape sequences.
   */
  static List<CodeLocation> extractCodeLocations(String str) throws Exception {
    ArrayList<CodeLocation> back = new ArrayList<CodeLocation>();
    if (str == null) {
      return back;
    }
    
    Matcher m = finder.matcher(str);
    int offset = 0;
    int maxOffset = str.length() - 3;
    while (offset <  maxOffset && m.find(offset)) {
      String n = m.group(2);
      int code =  (n.length() == 0) ? 0 : Integer.parseInt(n);
      if (m.group(4) == null) {
        back.add(new CodeLocation(code, m.start(), m.end()));
      } else {
        back.add(new CodeLocation(code, m.start(), m.end(2)));
        back.add(new CodeLocation(Integer.parseInt(m.group(4)), m.start(4), m.end()));
      }
      offset = m.end() + 1;
    }
    return back;
  }

  //---------------------------------------------------------------------------
  //-- Color tools
  
  private static final int foreground0 = 30;
  private static final int background0 = 40;

  private static Color swtColorOf(int ansiCode, Display display) throws Exception {
    switch(ansiCode) {
    case 0 : return display.getSystemColor(SWT.COLOR_BLACK);
    case 1 : return display.getSystemColor(SWT.COLOR_RED);
    case 2 : return display.getSystemColor(SWT.COLOR_GREEN);
    case 3 : return display.getSystemColor(SWT.COLOR_YELLOW);
    case 4 : return display.getSystemColor(SWT.COLOR_BLUE);
    case 5 : return display.getSystemColor(SWT.COLOR_MAGENTA);
    case 6 : return display.getSystemColor(SWT.COLOR_CYAN);
    case 7 : return display.getSystemColor(SWT.COLOR_WHITE);
    case 8 : //TODO implement custom color 8, until supported same as default
    case 9 : return null;
    }
    throw new IllegalArgumentException("ansi color not supported :" + ansiCode);
  }

  
  static TextStyle update(TextStyle currentStyle, int code, Display display) throws Exception {
    if (foreground0 <= code && code <= foreground0 + 9) {
      currentStyle.foreground = swtColorOf(code - foreground0, display);
    } else if (background0 <= code && code <= background0 + 9) {
      currentStyle.background = swtColorOf(code - background0, display);
    } else if (code == 0) {
      currentStyle.underline =  false;
      currentStyle.strikeout = false;
      currentStyle.borderStyle = SWT.NONE;
      currentStyle.font = null;
      currentStyle.foreground = null;
      currentStyle.background = null;
    } else if (code == 4) {
      currentStyle.underline =  true;
      currentStyle.underlineColor = currentStyle.foreground;
      currentStyle.underlineStyle = SWT.UNDERLINE_SINGLE;
    } else if (code == 21) {
      currentStyle.underline =  true;
      currentStyle.underlineColor = currentStyle.foreground;
      currentStyle.underlineStyle = SWT.UNDERLINE_DOUBLE;
    } else if (code == 24) {
      currentStyle.underline =  false;
    } else if (code == 7) {
      Color tmp = currentStyle.foreground;
      currentStyle.foreground = currentStyle.background;
      currentStyle.background = tmp;
      currentStyle.underlineColor = tmp;
    }
    return currentStyle;
  }
}