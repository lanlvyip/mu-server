package io.muserver;

import java.util.*;

import static io.muserver.Mutils.notNull;
import static io.muserver.NettyRequestParameters.isTruthy;
import static java.util.Arrays.asList;

public class MuHeaders extends Headers implements RequestParameters {

    private final Map<String, List<String>> all = new HashMap<>();

    @Override
    public Map<String, List<String>> all() {
        return all;
    }

    @Override
    public String get(String name) {
        return get(name, "");
    }

    @Override
    public String get(String name, String defaultValue) {
        List<String> matches = getAll(name);
        return matches.isEmpty() ? defaultValue : matches.get(0);
    }

    @Override
    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(get(name, ""), 10);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public long getLong(String name, long defaultValue) {
        try {
            return Long.parseLong(get(name, ""), 10);
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public float getFloat(String name, float defaultValue) {
        try {
            return Float.parseFloat(get(name, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public double getDouble(String name, double defaultValue) {
        try {
            return Double.parseDouble(get(name, ""));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    @Override
    public boolean getBoolean(String name) {
        String val = get(name, "").toLowerCase();
        return isTruthy(val);
    }

    @Override
    public List<String> getAll(String name) {
        List<String> vals = all.get(name.toLowerCase());
        return vals == null ? Collections.emptyList() : vals;
    }

    @Override
    public boolean contains(String name) {
        return all.containsKey(name.toLowerCase());
    }


    public MuHeaders set(String header, String value) {
        notNull("name", header);
        notNull("value", value);
        all.put(header.toLowerCase(), newList(value));
        return this;
    }


    public MuHeaders add(String header, String value) {
        notNull("name", header);
        notNull("value", value);
        header = header.toLowerCase();
        if (all.containsKey(header)) {
            all.get(header).add(value);
        } else {
            set(header, value);
        }
        return this;
    }


    public int size() {
        return all.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MuHeaders muHeaders = (MuHeaders) o;
        return Objects.equals(all, muHeaders.all);
    }

    @Override
    public int hashCode() {
        return Objects.hash(all);
    }

    @Override
    public String toString() {
        return all.toString();
    }


    public static final MuHeaders EMPTY = new MuHeaders() {
        @Override
        public Headers add(String name, Object value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers add(CharSequence name, Object value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers add(String name, Iterable<?> values) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers add(CharSequence name, Iterable<?> values) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers add(Headers headers) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers addInt(CharSequence name, int value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers addShort(CharSequence name, short value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers set(String name, Object value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers set(CharSequence name, Object value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers set(String name, Iterable<?> values) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers set(CharSequence name, Iterable<?> values) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers set(Headers headers) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers setAll(Headers headers) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers setInt(CharSequence name, int value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers setShort(CharSequence name, short value) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers remove(String name) {
            throw new UnsupportedOperationException("This is a readonly object");
        }

        @Override
        public Headers remove(CharSequence name) {
            throw new UnsupportedOperationException("This is a readonly object");
        }
    };


    @Override
    @Deprecated
    public String get(CharSequence name) {
        return get(name.toString());
    }

    @Override
    @Deprecated
    public String get(CharSequence name, String defaultValue) {
        return get(name.toString(), defaultValue);
    }

    @Override
    @Deprecated
    public Integer getInt(CharSequence name) {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public int getInt(CharSequence name, int defaultValue) {
        return getInt(name.toString(), defaultValue);
    }

    @Override
    @Deprecated
    public Short getShort(CharSequence name) {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public short getShort(CharSequence name, short defaultValue) {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public Long getTimeMillis(CharSequence name) {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public long getTimeMillis(CharSequence name, long defaultValue) {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public List<String> getAll(CharSequence name) {
        return getAll(name.toString());
    }

    @Override
    @Deprecated
    public List<Map.Entry<String, String>> entries() {
        if (isEmpty()) {
            return Collections.emptyList();
        }
        List<Map.Entry<String, String>> entriesConverted = new ArrayList<>(all.size());
        for (Map.Entry<String, String> entry : this) {
            entriesConverted.add(entry);
        }
        return entriesConverted;
    }

    @Override
    @Deprecated
    public Iterator<Map.Entry<String, String>> iterator() {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public Iterator<Map.Entry<CharSequence, CharSequence>> iteratorCharSequence() {
        throw new MuException("Not implemented");
    }

    @Override
    @Deprecated
    public boolean contains(CharSequence name) {
        return contains(name.toString());
    }

    @Override
    public boolean isEmpty() {
        return all.isEmpty();
    }

    @Override
    @Deprecated
    public Set<String> names() {
        return all.keySet();
    }

    @Override
    public Headers add(String name, Object value) {
        return add(name, valueToString(value));
    }

    private static ArrayList<String> newList(String s) {
        ArrayList<String> values = new ArrayList<>(1);
        values.add(s);
        return values;
    }

    private static String valueToString(Object value) {
        notNull("value", value);
        return value instanceof Date ? Mutils.toHttpDate((Date)value) : value.toString();
    }

    @Override
    @Deprecated
    public Headers add(CharSequence name, Object value) {
        return add(name.toString(), value);
    }

    @Override
    public Headers add(String name, Iterable<?> values) {
        List<String> cur = addIfAbsent(name);
        for (Object next : values) {
            cur.add(valueToString(next));
        }
        return this;
    }

    @Override
    @Deprecated
    public Headers add(CharSequence name, Iterable<?> values) {
        return add(name.toString(), values);
    }

    @Override
    @Deprecated
    public Headers add(Headers headers) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers addInt(CharSequence name, int value) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers addShort(CharSequence name, short value) {
        throw new MuException("Deprecated");
    }

    @Override
    public Headers set(String name, Object value) {
        return set(name, valueToString(value));
    }

    @Override
    @Deprecated
    public Headers set(CharSequence name, Object value) {
        throw new MuException("Deprecated");
    }

    @Override
    public Headers set(String name, Iterable<?> values) {
        remove(name);
        return add(name, values);
    }

    private List<String> addIfAbsent(String name) {
        List<String> values = all.get(name);
        if (values == null) {
            values = new ArrayList<>(1);
            all.put(name, values);
        }
        return values;
    }

    @Override
    @Deprecated
    public Headers set(CharSequence name, Iterable<?> values) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers set(Headers headers) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers setAll(Headers headers) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers setInt(CharSequence name, int value) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Headers setShort(CharSequence name, short value) {
        throw new MuException("Deprecated");
    }

    @Override
    public Headers remove(String name) {
        notNull("name", name);
        all.remove(name.toLowerCase());
        return this;
    }

    @Override
    @Deprecated
    public Headers remove(CharSequence name) {
        throw new MuException("Deprecated");
    }

    @Override
    public Headers clear() {
        all.clear();
        return this;
    }

    @Override
    @Deprecated
    public boolean contains(String name, String value, boolean ignoreCase) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public boolean containsValue(CharSequence name, CharSequence value, boolean ignoreCase) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public String getAsString(CharSequence name) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public List<String> getAllAsString(CharSequence name) {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public Iterator<Map.Entry<String, String>> iteratorAsString() {
        throw new MuException("Deprecated");
    }

    @Override
    @Deprecated
    public boolean contains(CharSequence name, CharSequence value, boolean ignoreCase) {
        throw new MuException("Deprecated");
    }

    @Override
    public boolean hasBody() {
        return contains(HeaderNames.TRANSFER_ENCODING) || getInt(HeaderNames.CONTENT_LENGTH, -1) > 0;
    }
}