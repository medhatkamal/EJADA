package org.example;

import org.json.JSONObject;
import org.json.XML;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class XMLToJsonConverter {

   public static void main(String[] args) {
      try {
         // Load the XML file
         File xmlFile = new File("src/main/resources/XMLResponseData.xml"); // Update this path if needed

         // Convert XML to JSON
         JSONObject jsonObject = convertXMLToJson(xmlFile);

         // Retrieve values
         int decisionFlowId = getDecisionFlowId(jsonObject);
         int score = getScore(jsonObject);

         // Print the values
         System.out.println("DecisionFlowId: " + decisionFlowId);  // Expected Result = 12
         System.out.println("SCORE: " + score);  // Expected Result = 301

      } catch (Exception e) {
         e.printStackTrace();
      }
   }

   public static JSONObject convertXMLToJson(File xmlFile) throws Exception {
      // Read the XML file content
      String content = new String(Files.readAllBytes(Paths.get(xmlFile.getAbsolutePath())));

      // Convert XML string to JSON object
      return XML.toJSONObject(content);
   }

   public static int getDecisionFlowId(JSONObject jsonObject) {
      return jsonObject
         .getJSONObject("soap:Envelope")
         .getJSONObject("soap:Body")
         .getJSONObject("ProcessRequestResponse")
         .getJSONObject("Response")
         .getJSONObject("responseData")
         .getJSONObject("Results")
         .getJSONObject("ExecuteDecisionSmartResponse")
         .getJSONObject("ExecuteDecisionSmartResult")
         .getInt("DecisionFlowId");
   }

   public static int getScore(JSONObject jsonObject) {
      return jsonObject
         .getJSONObject("soap:Envelope")
         .getJSONObject("soap:Body")
         .getJSONObject("ProcessRequestResponse")
         .getJSONObject("Response")
         .getJSONObject("responseData")
         .getJSONObject("Results")
         .getJSONObject("ExecuteDecisionSmartResponse")
         .getJSONObject("ExecuteDecisionSmartResult")
         .getJSONArray("DecisionSmartResults")
         .toList()
         .stream()
         .map(obj -> new JSONObject((java.util.Map) obj))
         .filter(obj -> obj.getJSONObject("ResultOutput").getString("Name").equals("SCORE"))
         .mapToInt(obj -> obj.getJSONObject("ResultOutput").getInt("Value"))
         .findFirst()
         .orElseThrow(() -> new RuntimeException("SCORE not found"));
   }
}
