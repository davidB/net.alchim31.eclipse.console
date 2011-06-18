package net_alchim31_eclipse_console_ansicolor;

import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.widgets.Display;
import org.junit.Test;


public class AnsiCodeReaderTest {

  @Test
  public void extractCodeLocationsOfBlank() throws Exception{
    assertEquals(0, AnsiCodesReader.extractCodeLocations(null).size());
    assertEquals(0, AnsiCodesReader.extractCodeLocations("").size());
    assertEquals(0, AnsiCodesReader.extractCodeLocations("   \t \n").size());
  }

  @Test
  public void extractCodeLocationsOfStringWithNoAnsiCodes() throws Exception{
    assertEquals(0, AnsiCodesReader.extractCodeLocations("hfhfg hjhflj hjlggl").size());
    assertEquals(0, AnsiCodesReader.extractCodeLocations("Hello world").size());
  }

  @Test
  public void extractCodeLocationsOfStringWithAnsiCodes() throws Exception{
    assertEquals(3, AnsiCodesReader.extractCodeLocations("\u001B[31mhello \u001B[33mworl\u001B[0md!").size());
  }
  
  @Test
  public void extractCodeLocationsOfStringWithUnmanagedAnsiCodes() throws Exception{
    assertEquals(2, AnsiCodesReader.extractCodeLocations("\033[1mhttp://twitter.com/ohmyzsh\033[0m").size());
  }
  @Test
  public void extractCodeLocationsOfStringWith2AnsiCodes() throws Exception{
    assertEquals(3, AnsiCodesReader.extractCodeLocations("\033[0;32m         __                                     __   \033[0m\n").size());
    assertEquals(3, AnsiCodesReader.extractCodeLocations("\033[0;32m/ /_/ / / / /  / / / / / / /_/ /    / /_(__  ) / / / \033[0m\n").size()); 
  }
  @Test
  public void extractStyleRangeAtRightPosition() throws Exception {
    assertEquals(3, AnsiCodesReader.extractCodeLocations("\u001B[31mhello \u001B[33mworl\u001B[0md!").size());
    List<StyleRange> styles = AnsiCodesReader.extractStyleRange("\u001B[31mhello \u001B[33mworl\u001B[0md!", 0, Display.getDefault());
    assertEquals(3*2, styles.size());
    assertEquals(0, styles.get(0).start);
    assertEquals(5, styles.get(0).length);
    assertEquals(5, styles.get(1).start);
    assertEquals(6, styles.get(1).length);
    assertEquals(11, styles.get(2).start);
    assertEquals(5, styles.get(2).length);
    assertEquals(16, styles.get(3).start);
    assertEquals(4, styles.get(3).length);
    assertEquals(20, styles.get(4).start);
    assertEquals(4, styles.get(4).length);
    assertEquals(24, styles.get(5).start);
    assertEquals(2, styles.get(5).length);
  }

  @Test
  public void extractStyleRangeMergeSiblingAnsiCodes() throws Exception {
    assertEquals(3, AnsiCodesReader.extractCodeLocations("\033[0;32m         __                                     __   \033[0m\n").size());
    // 3 code but 0 and 1 are merged x 2 (style of code + style of following text)
    assertEquals((3 - 1) * 2, AnsiCodesReader.extractStyleRange("\033[0;32m         __                                     __   \033[0m\n", 0, Display.getDefault()).size());
  }

}
