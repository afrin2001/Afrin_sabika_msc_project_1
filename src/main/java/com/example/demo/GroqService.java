package com.example.demo;

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

            StringBuilder studentData = new StringBuilder();
            for (Student s : students) {
                studentData.append(s.toString()).append("\\n");
            }

            String prompt = "You are a Student Management AI.\n" +
                    "Student Data:\n" + studentData +
                    "\nUser: " + message;

            String jsonInput = "{\n" +
                    "  \"model\": \"llama3-8b-8192\",\n" +
                    "  \"messages\": [\n" +
                    "    {\"role\": \"user\", \"content\": \"" + prompt.replace("\"","\\\"") + "\"}\n" +
                    "  ]\n" +
                    "}";

            conn.getOutputStream().write(jsonInput.getBytes());

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream())
            );

            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            String result = response.toString();
            return result.split("\"content\":\"")[1].split("\"")[0];

        } catch (Exception e) {
            e.printStackTrace();
            return "AI Error";
        }
    }
}
