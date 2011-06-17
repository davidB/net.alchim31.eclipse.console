package net_alchim31_eclipse_console_ansicolor;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.swt.custom.StyledText;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "net.alchim31.eclipse.console.ansicolor"; //$NON-NLS-1$

  // The shared instance
  private static Activator plugin;

  /**
   * The constructor
   */
  public Activator() {
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext
   * )
   */
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext
   * )
   */
  public void stop(BundleContext context) throws Exception {
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   * 
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /**
   * Stores the page participants for the consoles. This allows us to find the
   * participant for a specific console later on (e.g. when a refresh is
   * required).
   */
  private Map<StyledText, ConsolePageParticipant> _viewers = new HashMap<StyledText, ConsolePageParticipant>();

  public void setViewer(StyledText viewer,
      ConsolePageParticipant consolePageParticipant) {
    _viewers.put(viewer, consolePageParticipant);
  }

  /**
   * Removes the specified page participant from all viewers using it.
   * 
   * @param participant
   *          This participant will be removed.
   */
  public void removeViewers(ConsolePageParticipant participant) {
    Set<StyledText> remove = new HashSet<StyledText>();

    for (Entry<StyledText, ConsolePageParticipant> entry : _viewers.entrySet()) {
      if (entry.getValue() == participant) {
        remove.add(entry.getKey());
      }
    }

    for (StyledText viewer : remove) {
      _viewers.remove(viewer);
    }
  }

}
