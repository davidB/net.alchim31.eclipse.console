package net_alchim31_eclipse_console_ansicolor;

import static org.junit.Assert.*;

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
  }

  @Test
  public void extractCodeLocationsOfStringWithAnsiCodes() throws Exception{
    assertEquals(0, AnsiCodesReader.extractCodeLocations("hfhfg hjhflj hjlggl").size());
  }

}
