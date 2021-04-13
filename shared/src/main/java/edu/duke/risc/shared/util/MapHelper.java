package edu.duke.risc.shared.util;

import java.util.Map;

/**
 * @author eason
 * @date 2021/3/27 19:21
 */
public class MapHelper {

    /**
     * Update maps
     * @param map map
     * @param unitType unitType
     * @param diff diff
     * @param <T> generic type
     */
    public static <T> void updateMap(Map<T, Integer> map, T unitType, int diff) {
        if (map.containsKey(unitType)) {
            int originVal = map.get(unitType);
            if (diff >= 0) {
                map.put(unitType, diff + originVal);
            } else {
                if (originVal + diff <= 0) {
                    map.remove(unitType);
                } else {
                    map.put(unitType, diff + originVal);
                }
            }
        } else {
            if (diff > 0) {
                map.put(unitType, diff);
            }
        }
    }

}
