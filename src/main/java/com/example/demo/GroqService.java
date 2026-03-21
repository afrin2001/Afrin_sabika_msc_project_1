package com.example.demo;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@Service
public class GroqService {

    @Value("${groq.api.key}")
    private String apiKey;

    public String getAIResponse(String message, List<Student> students) {

        try {
            URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + apiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            // ✅ Prepare student data
            StringBuilder studentData = new StringBuilder();
            for (Student s : students) {
                studentData.append(s.toString()).append("\n");
            }

            String prompt = "You are a Student Management AI.\n" +
                    "Student Data:\n" + studentData +
                    "\nUser: " + message;

            // ✅ Build JSON safely
            JSONObject requestBody = new JSONObject();
            requestBody.put("model", "llama3-8b-8192");

            JSONArray messages = new JSONArray();

            JSONObject systemMsg = new JSONObject();
            systemMsg.put("role", "system");
            systemMsg.put("content", "You are a helpful Student Management AI");

            JSONObject userMsg = new JSONObject();
            userMsg.put("role", "user");
            userMsg.put("content", prompt);

            messages.put(systemMsg);
            messages.put(userMsg);

            requestBody.put("messages", messages);

            // ✅ Send request
            OutputStream os = conn.getOutputStream();
            os.write(requestBody.toString().getBytes());
            os.flush();

            // ✅ Read response (handle error also)
            BufferedReader reader;
            if (conn.getResponseCode() >= 400) {
                reader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            }

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String result = response.toString();

            // ✅ DEBUG (safe place — before return)
            System.out.println("FULL RESPONSE: " + result);

            // ✅ Parse JSON
            JSONObject json = new JSONObject(result);

            // 🔴 Handle API error
            if (json.has("error")) {
                JSONObject error = json.getJSONObject("error");
                return "API Error: " + error.getString("message");
            }

            // ✅ Normal response
            JSONArray choices = json.getJSONArray("choices");

            if (choices.length() > 0) {
                return choices.getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            }

            return "No response from AI";

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
