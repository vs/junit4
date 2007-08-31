package org.slf4j.converter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

import javax.swing.JFileChooser;

public class Converter {

  private List<File> javaFiles;

  private AbstractMatcher matcher;

  private Writer writer;

  private String source;

  private int conversionType;

  /**
   * Run JCL to SLF4J conversion
   * 
   * @param args
   *          source folder directory optional
   * @throws IOException
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {

    Converter converter = new Converter();

    if (args.length > 0) {
      converter.source = args[0];
    } else {
      JFileChooser selector = new JFileChooser();
      selector.setDialogTitle("Source folder selector");
      selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      int res = selector.showOpenDialog(null);
      if (res == JFileChooser.APPROVE_OPTION) {
        File folder = selector.getSelectedFile();
        converter.source = folder.getAbsolutePath();
      } else {
        return;
      }
    }

    converter.conversionType = Constant.JCL_TO_SLF4J;
    if (converter.init()) {
      converter.convert(converter.javaFiles);
    }
  }

  /**
   * Ask for concrete matcher implementation depending on the conversion mode
   * Ask for user confirmation to convert the selected source directory if valid
   * Ask for user confirmation in case of number of files to convert > 1000
   * 
   * @return true if init operation complete
   * @throws IOException
   */
  public boolean init() throws IOException {
    matcher = AbstractMatcher.getMatcherImpl(conversionType);
    if (matcher == null) {
      return false;
    }

    writer = new Writer();

    File fileSource = new File(source);
    if (!fileSource.isDirectory()) {
      System.out.println("source path is not a valid source directory");
      return false;
    } else {
      BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
      System.out
          .println("RUNNING CONVERTER WILL REPLACE JAVA FILES CONTAINED IN "
              + source + ", DO YOU WANT TO CONTINUE Y / N ?");
      String response = in.readLine();
      if (response.equalsIgnoreCase("N")) {
        return false;
      }

      selectFiles(fileSource);

      if (javaFiles.size() > Constant.NB_FILES_MAX) {
        System.out.println("THERE IS " + javaFiles.size()
            + " FILES TO CONVERT, DO YOU WANT TO CONTINUE Y / N ?");
        response = in.readLine();
        if (response.equalsIgnoreCase("N")) {
          return false;
        }
      }
    }
    return true;
  }

  /**
   * delete a file
   * 
   * @param fdest
   */
  private void delete(File fdest) {
    if (fdest.isDirectory()) {
      File[] files = fdest.listFiles();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          delete(files[i]);
        }
      }
      fdest.delete();
    } else {
      fdest.delete();
    }
  }

  /**
   * copy a file from source to destination
   * 
   * @param fsource
   * @param fdest
   */
  private void copy(File fsource, File fdest) {
    try {
      FileInputStream fis = new FileInputStream(fsource);
      FileOutputStream fos = new FileOutputStream(fdest);
      FileChannel channelSource = fis.getChannel();
      FileChannel channelDest = fos.getChannel();
      if (channelSource.isOpen() && channelDest.isOpen()) {
        channelSource.transferTo(0, channelSource.size(), channelDest);
        channelSource.close();
        channelDest.close();
      } else {
        System.out.println("error copying file " + fsource.getAbsolutePath());
      }

    } catch (FileNotFoundException exc) {
      System.out.println(exc.toString());
    } catch (IOException e) {
      System.out.println(e.toString());
    }
  }

  /**
   * Select java files to be converted
   * 
   * @param file
   * @return
   */
  private List<File> selectFiles(File file) {
    if (javaFiles == null) {
      javaFiles = new ArrayList<File>();
    }
    if (file.isDirectory()) {
      File[] files = file.listFiles();
      if (files != null) {
        for (int i = 0; i < files.length; i++) {
          selectFiles(files[i]);
        }
      }
    } else {
      if (file.getName().endsWith(".java")) {
        javaFiles.add(file);
      }
    }
    return javaFiles;
  }

  /**
   * Convert a list of files
   * 
   * @param lstFiles
   */
  private void convert(List<File> lstFiles) {
    Iterator<File> itFile = lstFiles.iterator();
    while (itFile.hasNext()) {
      File currentFile = itFile.next();
      convert(currentFile);
    }
  }

  /**
   * Convert the specified file Read each line and ask matcher implementation
   * for conversion Rewrite the line returned by matcher
   * 
   * @param file
   */
  private void convert(File file) {
    File newFile = new File(file.getAbsolutePath() + "new");
    try {
      boolean isEmpty = false;
      writer.initFileWriter(newFile);
      FileReader freader = new FileReader(file);
      BufferedReader breader = new BufferedReader(freader);
      String line;
      String newLine;
      while (!isEmpty) {
        line = breader.readLine();
        if (line != null) {
          newLine = matcher.getReplacement(line);
          writer.write(newLine);
        } else {
          isEmpty = true;
          writer.closeFileWriter();
          copy(newFile, file);
          delete(newFile);
        }
      }
    } catch (IOException exc) {
      System.out.println("error reading file " + exc);
    }
  }
}
