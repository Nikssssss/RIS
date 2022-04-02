package task2.database.dto;

public class TagDTO {
    private String key;
    private String value;
    private long nodeId;

    public TagDTO(String key, String value, long nodeId) {
        this.key = key;
        this.value = value;
        this.nodeId = nodeId;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public long getNodeId() {
        return nodeId;
    }
}
