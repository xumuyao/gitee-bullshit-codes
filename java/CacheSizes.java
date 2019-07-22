import java.io.OutputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class CacheSizes {

    /**
     * Returns the size in bytes of a basic Object. This method should only
     * be used for actual Object objects and not classes that extend Object.
     *
     * @return the size of an Object.
     */
    public static int sizeOfObject() {
        return 4;
    }

    /**
     * Returns the size in bytes of a String.
     *
     * @param string the String to determine the size of.
     * @return the size of a String.
     */
    public static int sizeOfString(String string) {
        if (string == null) {
            return 0;
        }
        return 4 + string.getBytes().length;
    }

    /**
     * Returns the size in bytes of a primitive int.
     *
     * @return the size of a primitive int.
     */
    public static int sizeOfInt() {
        return 4;
    }

    /**
     * Returns the size in bytes of a primitive char.
     *
     * @return the size of a primitive char.
     */
    public static int sizeOfChar() {
        return 2;
    }

    /**
     * Returns the size in bytes of a primitive boolean.
     *
     * @return the size of a primitive boolean.
     */
    public static int sizeOfBoolean() {
        return 1;
    }

    /**
     * Returns the size in bytes of a primitive long.
     *
     * @return the size of a primitive long.
     */
    public static int sizeOfLong() {
        return 8;
    }

    /**
     * Returns the size in bytes of a primitive double.
     *
     * @return the size of a primitive double.
     */
    public static int sizeOfDouble() {
        return 8;
    }

    /**
     * Returns the size in bytes of a Date.
     *
     * @return the size of a Date.
     */
    public static int sizeOfDate() {
        return 12;
    }

    /**
     * Returns the size in bytes of a Map object. 
     *
     * @param map the Map object to determine the size of.
     * @return the size of the Map object.
     * @exception Exception exception
     */
    public static int sizeOfMap(Map<?, ?> map)
        throws Exception {
        if (map == null) {
            return 0;
        }
        // Base map object -- should be something around this size.
        int size = 36;
        Set<? extends Map.Entry<?, ?>> set = map.entrySet();
        
        // Add in size of each value
        for (Map.Entry<?, ?> entry : set) {
            size += sizeOfAnything(entry.getKey());
            size += sizeOfAnything(entry.getValue());
        }
        return size;
    }

    /**
     * Returns the size in bytes of a Collection object. Elements are assumed to be
     * <tt>String</tt>s, <tt>Long</tt>s or <tt>Cacheable</tt> objects.
     *
     * @param list the Collection object to determine the size of.
     * @return the size of the Collection object.
     * @exception Exception exception
     */
    public static int sizeOfCollection(Collection<?> list) 
            throws Exception {
        if (list == null) {
            return 0;
        }
        // Base list object (approximate)
        int size = 36;
        // Add in size of each value
        Object[] values = list.toArray();
        for (int i = 0; i < values.length; i++) {
            size += sizeOfAnything(values[i]);
        }
        return size;
    }

    /**
     * Returns the size of an object in bytes. Determining size by serialization
     * is only used as a last resort.
     * @param object object
     * @return the size of an object in bytes.
     * @exception Exception Exception
     */
    public static int sizeOfAnything(Object object) 
        throws Exception {
        // If the object is Cacheable, ask it its size.
        if (object == null) {
            return 0;
        } else if (object instanceof String) {
            return sizeOfString((String)object);
        } else if (object instanceof Long) {
            return sizeOfLong();
        } else if (object instanceof Integer) {
            return sizeOfObject() + sizeOfInt();
        } else if (object instanceof Double) {
            return sizeOfObject() + sizeOfDouble();
        } else if (object instanceof Boolean) {
            return sizeOfObject() + sizeOfBoolean();
        } else if (object instanceof Map) {
            return sizeOfMap((Map<?, ?>)object);
        } else if (object instanceof long[]) {
            long[] array = (long[])object;
            return sizeOfObject() + array.length * sizeOfLong();
        } else if (object instanceof Collection) {
            return sizeOfCollection((Collection<?>)object);
        } else if (object instanceof byte[]) {
            byte[] array = (byte[])object;
            return sizeOfObject() + array.length;
        } else {
//            int size;
//            // Default to serializing the object out to determine size.
//            NullOutputStream out = new NullOutputStream();
//            ObjectOutputStream outObj = new ObjectOutputStream(out);
//            outObj.writeObject(object);
//            size = out.size();

            return sizeof(object);
        }
    }
    
    /**
     * 简单类型元素长度
     * @param clazz clazz
     * @return  int
     */
    private static int sizeofPrimitiveClass(Class<?> clazz) {
        return clazz == boolean.class || clazz == byte.class ? 1 : clazz == char.class || clazz == short.class ? 2
                : clazz == int.class || clazz == float.class ? 4 : 8;
    }
    
    /**
     * 简单类型数组长度
     * @param object object
     * @return int 
     */
    private static int lengthOfPrimitiveArray(Object object) {

        Class<?> clazz = object.getClass();

        return clazz == boolean[].class ? ((boolean[]) object).length
                : clazz == byte[].class ? ((byte[]) object).length
                : clazz == char[].class ? ((char[]) object).length : clazz == short[].class ? ((short[]) object).length
                : clazz == int[].class ? ((int[]) object).length : clazz == float[].class ? ((float[]) object).length
                : clazz == long[].class ? ((long[]) object).length : ((double[]) object).length;
    }

    /**
     * 构造器函数
     * @param object object
     * @param field field
     * @return int
     */
    private static int sizeofConstructor(Object object, Field field) {
        throw new UnsupportedOperationException("field type Constructor not accessible: " + object.getClass() + " field:" + field);
    }

    /**
     * 对象大小
     * @param object object
     * @return int
     */
    public static int sizeof(Object object) {
        if (object == null)
            return 0;
        int size = 8;
        Class<?> clazz = object.getClass();
        if (clazz.isArray()) {
            size += 16; // length变量是int型
            Class<?> componentType = clazz.getComponentType();
            if (componentType.isPrimitive())
                return size + lengthOfPrimitiveArray(object) * sizeofPrimitiveClass(componentType);
            Object[] array = (Object[]) object;
            size += 4 * array.length;
            for (Object o : array)
                size += sizeof(o);
            return size;
        }
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers()))
                continue; // 类成员不计
            Class<?> type = field.getType();
            if (type.isPrimitive())
                size += sizeofPrimitiveClass(type);
            else {
                size += 4; // 一个引用型变量占用4个字节
                try {
                    field.setAccessible(true); // 可以访问非public类型的变量
                    size += sizeof(field.get(object));
                } catch (Exception e) {
                    size += sizeofConstructor(object, field);
                }
            }
        }
        return size;
    }

    /**
     * 空的输出流（不输出）
     * 
     */
    public static class NullOutputStream extends OutputStream {

        /**
         * 数据大小
         */
        int size = 0;

        @Override
        public void write(int b) {
            size++;
        }

        @Override
        public void write(byte[] b) {
            size += b.length;
        }

        @Override
        public void write(byte[] b, int off, int len) {
            size += len;
        }

        /**
         * Returns the number of bytes written out through the stream.
         *
         * @return the number of bytes written to the stream.
         */
        public int size() {
            return size;
        }
    }
}
