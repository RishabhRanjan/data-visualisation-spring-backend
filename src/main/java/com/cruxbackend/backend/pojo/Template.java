package com.cruxbackend.backend.pojo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Template implements Serializable {
  public static List<Map<String, Object>> functionsDesc() {
    List<Map<String, Object>> functions = new ArrayList<>();

    Map<String, Object> function = new HashMap<>();
    function.put("name", "getChartsConfig");
    function.put("description", "get charts configuration for @ant-design/charts");

    Map<String, Object> parameters = new HashMap<>();
    parameters.put("type", "object");
    parameters.put("description",
        "An array of objects containing list of possible charts in decreasing order of relevance for the asked query. Atleast 2 elements are required");

    Map<String, Object> propertiesOuter = new HashMap<>();
    Map<String, Object> heading = new HashMap<>();
    heading.put("type", "string");
    heading.put("description", "A short concise heading relevant to the asked query. 4 words maximum");

    propertiesOuter.put("heading", heading);

    Map<String, Object> charts = new HashMap<>();
    charts.put("type", "array");
    charts.put("description", "chart configurations for individual chart types");

    Map<String, Object> items = new HashMap<>();
    items.put("type", "object");
    items.put("description", "values for chart configuration for each type of charts");
    items.put("required", List.of("chartType", "xField", "yField"));

    Map<String, Object> propertiesInner = new HashMap<>();

    Map<String, Object> chartType = new HashMap<>();
    chartType.put("type", "string");
    chartType.put("enum",
        List.of("lineChart", "pieChart", "barChart", "bubbleChart", "heatMap", "multiBar", "multiLine"));

    Map<String, Object> title = new HashMap<>();
    title.put("type", "string");
    title.put("description", "Relevant title for the graph");

    Map<String, Object> xField = new HashMap<>();
    xField.put("type", "string");
    xField.put("description",
        "The name of the data field corresponding to the graph in the x direction, usually the field corresponding to the horizontal coordinate axis. For example, to see how many people are in different classes, the class field is the corresponding xField.");

    Map<String, Object> yField = new HashMap<>();
    yField.put("type", "string");
    yField.put("description",
        "The name of the data field corresponding to the graph in the y direction, usually the field corresponding to the vertical coordinate axis. For example, to see the number of students in different classes, the number field is the corresponding yField");

    Map<String, Object> seriesField = new HashMap<>();
    seriesField.put("type", "string");
    seriesField.put("description",
        "Group fields. Metric requirements to see different situations in a dimension simultaneously. For example, if we look at the sales trends for the last 30 days in different regions, the region field is SeriesField. Value of this should be null if not applicable");

    Map<String, Object> formatter = new HashMap<>();
    formatter.put("type", "string");
    formatter.put("description",
        "A javascriptFunction that is a callback to format the data and group the rows to make the x values unique, the y value should be obtained by sum/count/max/min/avg depending on the chart. The result of this callback should be an array of { x: string | number, y: number } objects");

    propertiesInner.put("chartType", chartType);
    propertiesInner.put("title", title);
    propertiesInner.put("xField", xField);
    propertiesInner.put("yField", yField);
    propertiesInner.put("seriesField", seriesField);
    propertiesInner.put("javascriptFunction", formatter);

    items.put("properties", propertiesInner);

    charts.put("items", items);

    propertiesOuter.put("charts", charts);
    parameters.put("properties", propertiesOuter);
    function.put("parameters", parameters);

    functions.add(function);
    return functions;
  }

  public static final String instructionsv2 = "The following is a conversation between a Human and an AI assistant expert on data analysis and visualization with perfect javascript syntax."
      + " The human will provide a sample of a dataset for the AI to use as source."
      + " The real dataset that the human will use with the response of the AI its going to have several more rows."
      + "\n Instructions:"
      + "\n 1. Output a list of function arguments for atleast 2 different kind of charts in decreasing order of relevance inside the charts key of the function argument object."
      + "\n 2. Rules for different chart types are as follows:"
      + "\n 2.a. Bar/MultiBar chart should have 1 categorical value + 1 or multiple numerical value(of same unit)"
      + "\n 2.b Line/MultiLine chart should have 1 categorical value + 1 or multiple numerical value(of same unit)"
      + "\n 2.c. Pie chart should have 1 categorical value + 1 numerical value"
      + "\n 2.d. Bubble chart can have values from either of the three types mentioned a. 1 categorical + 1 or 2 numerical b. 2 categorical + 1 numerical c. 2 or 3 numerical"
      + "\n 2.e. HeatMap can have values from either of the three types mentioned a. 1 categorical + 1 or 2 numerical b. 2 categorical + 1 numerical c. 2 or 3 numerical"
      + "\n 3. All the source values are strings, so before operating over any number in the javascriptFunction, the algorithm should first transform the string by using the function parseFloat."
      + "\n 4. All the functions generated by this AI should have the form data => { return ... }."
      + "\n 5. In the functions, if a key of an object has to be accessed it should use obj['key'] notation"
      + "\n 6. Answer the question using the below data:\n";
}
