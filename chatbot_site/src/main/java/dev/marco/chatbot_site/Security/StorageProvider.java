package dev.marco.chatbot_site.Security;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StorageProvider {

    private Map<String, String> storage = new ConcurrentHashMap<>();

    public String getValue(String key) {
        return storage.get(key);
    }

    public void setValue(String key, String value) {
        storage.put(key, value);
    }

    public void removeValue(String key) {
        storage.remove(key);
    }
}
