package net_alchim31_eclipse_console_ansicolor;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsolePageParticipant;
import org.eclipse.ui.part.IPageBookViewPage;

public class ConsolePageParticipant implements IConsolePageParticipant {

  @Override
  public Object getAdapter(Class arg0) {
    return null;
  }

  @Override
  public void activated() {
  }

  @Override
  public void deactivated() {
  }

  /**
   * Removes the viewer from the plugin's viewer map on disposal.
   * 
   * @see org.eclipse.ui.console.IConsolePageParticipant#dispose()
   */
  @Override
  public void dispose() {
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
      viewer.addLineStyleListener(new AnsiStyleListener());
    }

  }

}
