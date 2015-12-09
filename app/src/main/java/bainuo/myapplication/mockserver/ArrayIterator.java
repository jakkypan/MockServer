/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication.mockserver;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 * array迭代，给array的值加上区分后缀【只有String类型加】
 *
 * Created by panhongchao on 15/12/7.
 */
public class ArrayIterator {

    private static int suffixInt = 0;

    public static Object iterator(Object oldArray) throws Exception {

        // 针对array数组的处理
        if (oldArray.getClass().isArray()) {
            int len = Array.getLength(oldArray);
            for (int i = 0; i < len; i++) {
                suffixInt = i;
                Object itemArray = Array.get(oldArray, i);
                // 重写这个item
                Array.set(oldArray, i, iterator(itemArray));
            }
        } else if (oldArray instanceof IMockModel) {
            // 针对obj
            Field[] fields = oldArray.getClass().getFields();
            // 必须新建个实例对象
            Object newItemArray = oldArray.getClass().newInstance();
            for (Field field : fields) {
                field.setAccessible(true);
                Object fo = field.get(oldArray);
                if (fo instanceof String) {
                    String str = (String) fo;
                    str = str + String.valueOf(suffixInt);
                    field.set(newItemArray, str);
                } else if (fo instanceof String[]) {
                    String[] oldStrs = (String[]) fo;
                    // 必须新建个实例对象
                    String[] newStrs = new String[oldStrs.length];
                    for (int i = 0; i < oldStrs.length; i++) {
                        newStrs[i] = oldStrs[i] + String.valueOf(suffixInt);
                    }
                    field.set(newItemArray, newStrs);
                } else if (fo instanceof IMockModel) {
                    // 如果子属性仍是obj，则递归进去
                    field.set(newItemArray, iterator(fo));
                } else {
                    // 其他类型的不做修改
                    field.set(newItemArray, fo);
                }
                field.setAccessible(false);
            }
            oldArray = newItemArray;
        }

        return oldArray;
    }
}
