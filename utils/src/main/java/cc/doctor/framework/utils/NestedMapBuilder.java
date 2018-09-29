package cc.doctor.framework.utils;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 嵌套map工具，todo：增加校验
 */
public class NestedMapBuilder {
    private MapNode root;
    private MapNode current;

    public static NestedMapBuilder newMap() {
        NestedMapBuilder nestedMapBuilder = new NestedMapBuilder();
        nestedMapBuilder.root = MapNode.newNode();
        nestedMapBuilder.current = nestedMapBuilder.root;
        return nestedMapBuilder;
    }

    public NestedMapBuilder startSubMap(String key) {
        MapNode mapNode = MapNode.newNode();
        current.getMap().put(key, mapNode.getMap());
        mapNode.prev = current;
        current = mapNode;
        return this;
    }

    public NestedMapBuilder endSubMap() {
        current = current.prev;
        return this;
    }

    public NestedMapBuilder put(String key, Object value) {
        current.getMap().put(key, value);
        return this;
    }

    public Map<String, Object> build() {
        return root.getMap();
    }

    @Data
    static class MapNode {
        private Map<String, Object> map;
        private MapNode prev;

        static MapNode newNode() {
            MapNode mapNode = new MapNode();
            mapNode.map = new LinkedHashMap<>();
            mapNode.prev = mapNode;
            return mapNode;
        }
    }
}
