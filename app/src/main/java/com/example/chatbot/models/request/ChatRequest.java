// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.chatbot.models.request;
import java.util.List;

public class ChatRequest {
    private long maxTokens;
    private double temperature;
    private String model;
    private String prompt;

    public ChatRequest(int maxTokens, String model, String prompt, double temperature) {
        this.maxTokens=maxTokens;
        this.model=model;
        this.prompt=prompt;
        this.temperature=temperature;

    }

    public long getMaxTokens() { return maxTokens; }
    public void setMaxTokens(long value) { this.maxTokens = value; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double value) { this.temperature = value; }

    public String getModel() { return model; }
    public void setModel(String value) { this.model = value; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String value) { this.prompt = value; }
}
