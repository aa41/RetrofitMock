package com.xiaoma.retrofitmock;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jcf-mbp on 17-4-21.
 */
public class RetrofitMock {

    public static String[] overAllAttrs;
    public static final Map<String, String> attrMap = new HashMap<>();
    public static final HashMap<Class, Object> randomMap = new HashMap<>();

    public static List<Method> getAllMethod(Class cls) {
        List<Method> methods = new ArrayList<>();
        Method[] methodArray = cls.getDeclaredMethods();
        methods.addAll(Arrays.asList(methodArray));
        methods.addAll(getParentMethod(cls) != null ? getParentMethod(cls) : new ArrayList<Method>());
        return methods;
    }


    public static List<Method> getMockMethodList(Class cls, String... arrays) {
        List<Method> methods = new ArrayList<>();
        List<Method> allMethods = getAllMethod(cls);
        for (int i = 0; i < allMethods.size(); i++) {
            Method method = allMethods.get(i);
            if (isValidate(method, "set") ) {
                if (raidOfAttr(method, arrays)) {
                    methods.add(method);
                }
            }
        }
        return methods;
    }

    public static List<Field> getMockFieldList(Class cls, String... arrays) {
        List<Field> fields = new ArrayList<>();
        List<Field> allField = getAllField(cls);
        for (int i = 0; i < allField.size(); i++) {
            Field field = allField.get(i);
            for (String array : arrays) {
                if (field.getName().equals(array)) {
                    fields.add(field);
                }
            }
        }
        return fields;
    }

    public static void setAttrs(String... arrays) {
        overAllAttrs = arrays;
    }


    public static <T> T init(T obj) {
        Class<?> cls = obj.getClass();
        if (cls.isAnnotationPresent(Mock.class)) {
            Mock mockCls = (Mock) cls.getAnnotation(Mock.class);
            String[] attrs = mockCls.value();
            String[] copy;
            if (overAllAttrs != null && overAllAttrs.length > 0) {
                copy = Arrays.copyOf(attrs, overAllAttrs.length + attrs.length);
                System.arraycopy(overAllAttrs, 0, copy, attrs.length, overAllAttrs.length);
            } else {
                copy = attrs;
            }
            String[] findArrays = new String[copy.length];

            for (int i = 0; i < copy.length; i++) {
                String[] split = copy[i].split("=");
                if (split != null && split.length == 2) {
                    attrMap.put(split[0], split[1]);
                    findArrays[i] = split[0];
                }
            }
            try {
                initSetMockData(obj, cls, findArrays);

                List<Method> mockMethodList = getMockMethodList(cls, findArrays);
                for (int i = 0; i < mockMethodList.size(); i++) {
                    Method method = mockMethodList.get(i);
                    method.setAccessible(true);
                    Class paramTypes = getMethodParamTypes(method);
                    setRandomMockData(obj, method, paramTypes);

                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return obj;

    }


    private static <T> void initSetMockData(T obj, Class<?> cls, String[] findArrays) throws IllegalAccessException {
        List<Field> fieldList = getMockFieldList(cls, findArrays);
        for (int i = 0; i < fieldList.size(); i++) {
            Field field = fieldList.get(i);
            field.setAccessible(true);
            String type = field.getType().toString();

            if (type.contains("String")) {
                field.set(obj, attrMap.get(field.getName()));
            } else if (type.equals("int") || type.equals("Integer")) {
                field.set(obj, Integer.valueOf(attrMap.get(field.getName())));
            } else if (type.equals("double") || type.equals("Double")) {
                field.set(obj, Double.valueOf(attrMap.get(field.getName())));
            } else if (type.equals("float") || type.equals("Float")) {
                field.set(obj, Float.valueOf(attrMap.get(field.getName())));
            } else if (type.equals("boolean") || type.equals("Boolean")) {
                field.set(obj, Boolean.valueOf(attrMap.get(field.getName())));
            } else if (type.equals("long") || type.equals("Long")) {
                field.set(obj, Long.valueOf(attrMap.get(field.getName())));
            }
        }
    }


    private static boolean raidOfAttr(Method method, String... arrays) {
        for (String array : arrays) {
            if (method.getName().toLowerCase().equals("set" + array) || method.getName().toLowerCase().equals("is" + array)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isValidate(Method method, String startStr) {
        return method.getName().startsWith(startStr);
    }

    public static List<Field> getAllField(Class cls) {
        List<Field> fields = new ArrayList<>();
        Field[] declaredFields = cls.getDeclaredFields();
        fields.addAll(Arrays.asList(declaredFields));
        fields.addAll(getParentMethod(cls) != null ? getParentField(cls) : new ArrayList<Field>());
        return fields;
    }

    public static List<Field> getParentField(Class cls) {
        List<Field> list = new ArrayList<>();
        Class superclass = cls.getSuperclass();
        if (superclass != null) {
            Field[] declaredFields = superclass.getDeclaredFields();
            list.addAll(Arrays.asList(declaredFields));
            List<Field> parentField = getParentField(superclass);
            if (parentField != null) {
                list.addAll(parentField);
            }
            return list;
        }

        return null;
    }


    public static List<Method> getParentMethod(Class cls) {
        List<Method> methods = new ArrayList<>();
        Class superclass = cls.getSuperclass();
        if (superclass != null) {
            Method[] methodArray = superclass.getDeclaredMethods();
            methods.addAll(Arrays.asList(methodArray));
            List<Method> parentMethod = getParentMethod(superclass);
            if (parentMethod != null) {
                methods.addAll(parentMethod);
            }
            return methods;
        }
        return null;
    }

    public static Class getMethodParamTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes.length != 1) {
            return null;
        } else {
            return parameterTypes[0];
        }
    }


    public static void setRandomMockData(Object obj, Method method, Class cls) throws InvocationTargetException, IllegalAccessException {
        if(cls!=null){
            if (cls == String.class) {
                method.invoke(obj, getRandomString());
            } else if (cls == int.class || cls == Integer.class) {
                method.invoke(obj, getRandomInt());
            } else if (cls == double.class || cls == Double.class) {
                method.invoke(obj, getRandomDouble());
            } else if (cls == float.class || cls == Float.class) {
                method.invoke(obj, getRandomFloat());
            } else if (cls == boolean.class || cls == Boolean.class) {
                method.invoke(obj, getRandomBoolean());
            }else if(cls==long.class || cls== Long.class){
                method.invoke(obj,getRandomLong());
            } else if(!isJavaBasic(cls)){
                if(cls.isAssignableFrom(List.class)){

                }else{
                    
                }
            }
        }


    }

    private static boolean isJavaBasic(Class cls){
        return cls.isPrimitive() && cls==String.class && cls==Integer.class && cls ==Float.class && cls==Double.class &&
                cls==Long.class && cls==Boolean.class;
    }


    public static String getRandomString() {
        return (String) (randomMap.get(String.class) == null ? "老司机开车咯" : randomMap.get(String.class));
    }

    public static void setRandomString(String randomString) {
        randomMap.put(String.class, randomString);
    }


    public static void setRandomInt(int randomInt) {
        randomMap.put(Integer.class, randomInt);
    }

    public static Integer getRandomInt() {
        return Integer.valueOf(randomMap.get(Integer.class) == null ? (int) (Math.random() * 100) : (Integer) randomMap.get(Integer.class));
    }

    public static void setRandomDouble(double randomDouble) {
        randomMap.put(Double.class, randomDouble);
    }

    public static Double getRandomDouble() {
        return randomMap.get(Double.class) == null ? Math.random() * 100.0d : (Double) randomMap.get(Double.class);
    }

    public static void setRandomFloat(float randomFloat) {
        randomMap.put(Float.class, randomFloat);
    }

    public static Float getRandomFloat() {
        return (Float) (randomMap.get(Float.class) == null ? Math.random() * 100.0f : randomMap.get(Float.class));
    }


    public static void setRandomLong(long randomLong) {
        randomMap.put(Long.class, randomLong);
    }

    public static Long getRandomLong() {
        return (Long) (randomMap.get(Float.class) == null ? Math.random() * 100 : randomMap.get(Long.class));
    }

    public static void setRandomFloat(boolean randomBoolean) {
        randomMap.put(Boolean.class, randomBoolean);
    }

    public static Boolean getRandomBoolean() {
        return Math.random() < 1 / 2.0f;
    }


}
