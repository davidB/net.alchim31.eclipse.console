package net_alchim31_eclipse_console_ansicolor;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.custom.LineStyleEvent;
import org.eclipse.swt.custom.LineStyleListener;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

public class ConsolePageParticipant implements IConsolePageParticipant {

  private AnsiStyleListener _ansiStyler = new AnsiStyleListener();
  
  @Override
  public Object getAdapter(Class arg0) {
    return null;
  }

  @Override
  public void activated() {
    System.out.println("activated ConsolePageParticipant");
  }

  @Override
  public void deactivated() {
    System.out.println("deactivated ConsolePageParticipant");
  }

  /**
   * Removes the viewer from the plugin's viewer map on disposal.
   * 
   * @see org.eclipse.ui.console.IConsolePageParticipant#dispose()
   */
  @Override
  public void dispose() {
    System.out.println("dispose ConsolePageParticipant");
    Activator.getDefault().removeViewers(this);
  }

  /**
   * When initializing a participant for a console containing styled text, a
   * GrepConsoleStyleListener is attached to the console's viewer.
   * 
   * @see org.eclipse.ui.console.IConsolePageParticipant#init(org.eclipse.ui.part.IPageBookViewPage, org.eclipse.ui.console.IConsole)
   */
  @Override
  public void init(IPageBookViewPage page, IConsole console) {
    System.out.println("init ConsolePageParticipant");
    if (page.getControl() instanceof StyledText) {
      StyledText viewer = (StyledText) (page.getControl());
      Activator.getDefault().setViewer(viewer, this);
      viewer.addLineStyleListener(_ansiStyler);
    }

  }
  
  static class AnsiStyleListener implements LineStyleListener {
    
    // TODO optimize parsing and matching
    @Override
    public void lineGetStyle(LineStyleEvent event) {
      try {
        if (event.lineText.length() == 0) {
          return;
        }
        List<StyleRange> styles =  AnsiCodesReader.extractStyleRange(event.lineText, event.lineOffset, Display.getCurrent());
        // override current styles => should be listed after
        if (event.styles != null) {
          List<StyleRange> ansiStyles = styles;
          styles = new ArrayList<StyleRange>(event.styles.length + ansiStyles.size());
          for (StyleRange sr : event.styles) {
            styles.add(sr);
          }
          styles.addAll(ansiStyles);
        }
        event.styles = styles.toArray(new StyleRange[0]);
      } catch (Exception exc) {
        //TODO Log
        exc.printStackTrace();
      }
    }
  }

}
