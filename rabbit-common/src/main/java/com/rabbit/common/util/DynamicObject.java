package com.rabbit.common.util;

import org.springframework.cglib.beans.BeanGenerator;
import org.springframework.cglib.beans.BeanMap;

import java.util.*;

/**
 * @ClassName DynamicObject
 * @Description 动态对象创建工具
 * @Author LZQ
 * @Date 2019/1/20 19:53
 **/
public class DynamicObject {

    public static <T> DynamicObjectBuilder<T> createBuilder(Class<T> requiredType){
        return new DynamicObjectBuilder<>(requiredType);
    }

    public static class DynamicObjectBuilder<T> {
        private Class<T> requiredType;

        private Map<String, Class<?>> fields;

        private Map<String, Object> fieldValues;

        DynamicObjectBuilder(Class<T> requiredType) {
            this.requiredType = requiredType;
            this.fields = new HashMap<>();
            this.fieldValues = new HashMap<>();
        }

        /**
         * 将对象中的属性值赋值到相应的属性中
         * 注: 该方法不能增加属性,只能设置同名的属性的值.一般情况下传入 requiredType 类型的对象
         * @param obj 包含属性值的对象
         */
        public DynamicObjectBuilder<T> setFieldValueInObject(Object obj) {
            BeanMap beanMap = BeanMap.create(obj);
            for (Object key : beanMap.keySet()) {
                this.setFieldValue((String) key, beanMap.get(key));
            }
            return this;
        }

        /**
         * 添加Map中存储的属性名和属性值
         * 注: 增加 requiredType 中已经存在的属性时无效,也不能修改 requiredType 中已经存在的属性的类型,只能设置该属性值
         * @param fields 包含属性名和属性值的Map
         */
        public DynamicObjectBuilder<T> addFieldValueInMap(Map<String, Object> fields) {
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                this.addFieldValue(entry.getKey(), entry.getValue() == null ? Object.class : entry.getValue().getClass(), entry.getValue());
            }
            return this;
        }


        /**
         * 设置属性值
         * @param key 属性名
         * @param value 属性值
         */
        public DynamicObjectBuilder<T> setFieldValue(String key, Object value) {
            fieldValues.put(key, value);
            return this;
        }

        /**
         * 增加一个新的属性,并指定类型,属性名,属性值
         * 注: 增加 requiredType 中已经存在的属性时无效,也不能修改 requiredType 中已经存在的属性的类型,只能设置该属性值
         * @param key 属性名
         * @param type 类型
         * @param value 属性值
         */
        public DynamicObjectBuilder<T> addFieldValue(String key, Class<?> type, Object value) {
            fields.put(key, type);
            fieldValues.put(key, value);
            return this;
        }

        /**
         * 移除已添加的属性
         * 注: 不能移除 requiredType 中已经存在的属性
         * @param key 属性名
         */
        public DynamicObjectBuilder<T> removeField(String key) {
            if (fields.containsKey(key)) {
                fields.remove(key);
                fieldValues.remove(key);
            }
            return this;
        }

        @SuppressWarnings("unchecked")
        public T build() {
            BeanGenerator generator = new BeanGenerator();
            generator.setSuperclass(requiredType);
            // 添加属性
            for (Map.Entry<String, Class<?>> f : fields.entrySet()) {
                Object val = f.getValue();
                if (val == null) {
                    generator.addProperty(f.getKey(), Object.class);
                } else {
                    generator.addProperty(f.getKey(), f.getValue());
                }
            }
            Object obj = generator.create();
            // 设置属性值
            BeanMap objBeanMap = BeanMap.create(obj);
            for (Map.Entry<String, Object> fv : fieldValues.entrySet()) {
                Object val = fv.getValue();
                if (val != null) {
                    objBeanMap.put(fv.getKey(), val);
                }
            }
            if(requiredType != null && !requiredType.isAssignableFrom(obj.getClass())) {
                throw new RuntimeException("类型错误,需要[" + requiredType.getName() + "]类型,但是实际是[" + obj.getClass().getName() + "]类型!");
            } else {
                return (T)obj;
            }
        }
    }
}
