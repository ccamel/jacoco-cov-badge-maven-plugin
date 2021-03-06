package org.gahan;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import java.io.FileReader;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDMMType1Font;


/**
 * Utility class for generating badges.
 */
public class BadgeUtility {

  // jacoco csv report column number
  public static final int INSTRUCTION_MISSED_COL = 3;
  public static final int INSTRUCTION_COVERED_COL = 4;

  /**
   * Calculates width of given string in pixels. Font size currently is 11,
   * default
   *
   * @param str input string needed
   * @return width in pixels as floating point value
   */
  public static float calculateWidth(String str) throws IOException {
    PDFont font = PDMMType1Font.HELVETICA;
    int fontSize = 12;
    float width = ((font.getStringWidth(str) / 1000) * fontSize) + 10.0f;
    return width;
  }

  /**
   * Calcualte coverage %age from csv reprot generated by jacoco.
   *
   * @param csvPath Absolute path of csv report
   * @return integer value containing total coverage
   * @throws IOException Unable to read jacoco csv report from config location
   * @throws NumberFormatException Unable to parse %ages from jacoco reports
   */
  public static int calculateCoverage(String csvPath) throws NumberFormatException, IOException {
    // intialize reader
    CSVReader reader =
        new CSVReaderBuilder(new FileReader(csvPath)).withSkipLines(1).build();

    // counter for total instructions missed
    long instructionsMissed = 0;

    // counter for total instructions covered
    long instructionsCovered = 0;

    // loop thorugh csv and update counters
    String[] line;
    while (null != (line = reader.readNext())) {
      if (line[INSTRUCTION_MISSED_COL] != null && !line[INSTRUCTION_MISSED_COL].equals("")) {
        instructionsMissed += Integer.parseInt(line[INSTRUCTION_MISSED_COL]);
      }

      if (line[INSTRUCTION_COVERED_COL] != null && !line[INSTRUCTION_COVERED_COL].equals("")) {
        instructionsCovered += Integer.parseInt(line[INSTRUCTION_COVERED_COL]);
      }
    }
    reader.close();

    // calculate coverage
    float totalInstructions = instructionsCovered + instructionsMissed;
    float coveragePercent = (instructionsCovered / totalInstructions) * 100.0f;
    int coverage = (int) Math.floor(coveragePercent);
    return coverage;
  }

  /**
   * Get correct color for the given %age range.
   * %age range is mapped to a particular color.
   * <table>
   *   <tr>
   *     <td>Coverage %age</td>
   *     <td>Color Enum</td>
   *   </tr>
   *   <tr>
   *     <td>[0, 40)</td>
   *     <td>RED</td>
   *   </tr>
   *   <tr>
   *     <td>[40, 50)</td>
   *     <td>ORANGE</td>
   *   </tr>
   *   <tr>
   *     <td>[50, 60)</td>
   *     <td>YELLOW</td>
   *   </tr>
   *   <tr>
   *     <td>[60, 70)</td>
   *     <td>YELLOWGREEN</td>
   *   </tr>
   *   <tr>
   *     <td>[70, 80)</td>
   *     <td>GREEN</td>
   *   </tr>
   *   <tr>
   *     <td>[80, 100)</td>
   *     <td>BRIGHTGREEN</td>
   *   </tr>
   * </table>
   * @param coverage coverage percentage rounded off to nearest integer
   * @return An Enum for the given range
   * @see BadgeColors
   */
  public static BadgeColors getColorFromRange(int coverage) {
    // TODO: Grey for invalid coverage
    if (coverage >= 0 && coverage < 40) {
      return BadgeColors.RED;
    } else if (coverage >= 40 && coverage < 50) {
      return BadgeColors.ORANGE;
    } else if (coverage >= 50 && coverage < 60) {
      return BadgeColors.YELLOW;
    } else if (coverage >= 60 && coverage < 70) {
      return BadgeColors.YELLOWGREEN;
    } else if (coverage >= 70 && coverage < 80) {
      return BadgeColors.GREEN;
    } else {
      return BadgeColors.BRIGHTGREEN;
    }
  }
}
