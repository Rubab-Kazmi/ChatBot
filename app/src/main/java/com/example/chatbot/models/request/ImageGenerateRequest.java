// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.chatbot.models.request;

public class ImageGenerateRequest {
    private String size;
    private String prompt;
    private long n;

    public String getSize() { return size; }
    public void setSize(String value) { this.size = value; }

    public String getPrompt() { return prompt; }
    public void setPrompt(String value) { this.prompt = value; }

    public long getN() { return n; }
    public void setN(long value) { this.n = value; }
}
