package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Label;

public class LabelManager {
  // structure counters, used for unique label generation
  private int ifCount = 0;
  private int whileCount = 0;
  private int andCount = 0;
  private int orCount = 0;
  private int boolAssignCount = 0;
  private int boolExpCount = 0;

  /**
   * True if there is at least one call to an IO function, else False.
   */
  private boolean ioError = false;

  /**
   * True if there is a possibility of division by zero in the code, else False.
   */
  private boolean divByZeroError = false;

  /**
   * Prefix for the label.
   */
  private String prefix = new String();

  /**
   * Get a new LabelIf according to the current ifCount.
   * 
   * @return
   *         a LabelIf Object
   */
  public LabelIf getIf() {
    ifCount++;
    return new LabelIf(ifCount, this);
  }

  /**
   * Get a new LabelWhile according to the current whileCount.
   * 
   * @return
   *         a LabelWhile Object
   */
  public LabelWhile getWhile() {
    whileCount++;
    return new LabelWhile(whileCount, this);
  }

  /**
   * Get a new LabelAnd according to the current andCount.
   * 
   * @return
   *         a LabelAnd Object
   */
  public LabelAnd getAnd() {
    andCount++;
    return new LabelAnd(andCount, this);
  }

  /**
   * Get a new LabelOr according to the current orCount.
   * 
   * @return
   *         a LabelOr Object
   */
  public LabelOr getOr() {
    orCount++;
    return new LabelOr(orCount, this);
  }

  /**
   * Get a new LabelBoolAssign according to the current boolAssignCount.
   * 
   * @return
   *         a LabelBoolAssign Object
   */
  public LabelBoolAssign getBoolAssign() {
    boolAssignCount++;
    return new LabelBoolAssign(boolAssignCount, this);
  }

  /**
   * Get a new LabelBoolExp according to the current boolExpCount.
   * 
   * @return
   *         a LabelBoolExp Object
   */
  public LabelBoolExp getBoolExp() {
    boolExpCount++;
    return new LabelBoolExp(boolExpCount, this);
  }

  /**
   * Get the current prefix of the labels.
   * 
   * @return
   *         the current prefix
   */
  public String getPrefix() {
    return prefix;
  }

  /**
   * Set the prefix for the labels.
   * 
   * @param prefix
   *               the new prefix
   */
  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  /**
   * Get if there is a possibility of IO error in the code.
   * 
   * @return
   *         True if there is at least one call to an IO function, else False.
   */
  public boolean isIoError() {
    return ioError;
  }

  /**
   * Set the IO error possibility.
   * 
   * @param ioError
   *                the status of the IO error.
   */
  public void setIoError(boolean ioError) {
    this.ioError = ioError;
  }

  public Label getIoErrorLabel() {
    return new Label("io_error");
  }

  /**
   * Get if there is a possibility of division by zero in the code.
   * 
   * @return
   *         True if there is a possibility of division by zero in the code, else
   *         False.
   */
  public boolean isDivByZeroError() {
    return divByZeroError;
  }

  /**
   * Set the possibility of division by zero in the code.
   * 
   * @param divByZeroError the status of the division by zero error.
   */
  public void setDivByZeroError(boolean divByZeroError) {
    this.divByZeroError = divByZeroError;
  }

  public Label getDivByZeroError() {
    return new Label("div_zero_error");
  }

  public Label getStackOverflowError() {
    return new Label("stack_overflow_error");
  }
}
