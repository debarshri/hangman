package game.calc;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.*;

public class MapSorter {

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
        List<Map.Entry<K, V>> list =
                new LinkedList<>(map.entrySet());
        Collections.sort(list, (o1, o2) -> (o2.getValue()).compareTo(o1.getValue()));

        Map<K, V> result = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }


    public static List<String> read(String filename) throws IOException {

        MapSorter mapSorter = new MapSorter();
        InputStream resourceAsStream = mapSorter.getClass().getResourceAsStream(filename);
        StringWriter writer = new StringWriter();
        IOUtils.copy(resourceAsStream, writer);
        String data1 = writer.toString();

        return Arrays.asList(data1.split("\n"));
    }
}
