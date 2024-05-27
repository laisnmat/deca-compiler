package fr.ensimag.deca.codegen;

import java.util.HashMap;

public class LabelManager {

  public LabelManager() {
    this.labels = new HashMap<String, Integer>();
  }

  public HashMap<String, Integer> labels;

  public String getLabel(String labelName) {

    if (!this.labels.containsKey(labelName))
    {
      this.labels.put(labelName, 1);
    }
    else
    {
      int newVal = this.labels.get(labelName).intValue() + 1;
      Integer updated = new Integer(newVal);
      this.labels.put(labelName, updated);
    }
    return labelName.concat(labels.get(labelName).toString());
  }

  public String getErrorLabel(String error) {
    return null;
  }
  
}
