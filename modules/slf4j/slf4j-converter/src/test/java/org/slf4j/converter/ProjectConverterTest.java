package org.slf4j.converter;

import java.io.File;

import junit.framework.TestCase;

public class ProjectConverterTest extends TestCase {

  
  public void test() {
  }
  
  public void testBarracuda() {
    ProjectConverter pc = new ProjectConverter(Constant.LOG4J_TO_SLF4J);
    File projectFolder = new File("c:/home/ceki//Varia/Barracuda");
    pc.convertProject(projectFolder);
  }
}
