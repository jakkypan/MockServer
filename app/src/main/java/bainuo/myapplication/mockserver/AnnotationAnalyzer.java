/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication.mockserver;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import android.util.Log;

/**
 * 注释解析器
 * <p/>
 * Created by panhongchao on 15/12/2.
 */
public class AnnotationAnalyzer<T extends IMockModel> {
    public static final String TAG = "AnnotationProcessor";

    /**
     * 记录对象间的调用和被调用的堆栈关系
     */
    public Stack<Map<T, Object>> mParentModel = new Stack<>();

    /**
     * 记录{@link MockObjArray}被写入值的位置
     */
    public Map<Object, Integer> mArrIndex = new HashMap<>();

    /**
     * 记录需要增加区分后缀的数组对象
     */
    public List<T> mHasDiffObj = new ArrayList<>();

    public void fieldAnnotationValue(T t) {
        if (t == null) {
            return;
        }

        try {
            Field[] reflectFields = t.getClass().getFields();
            for (Field f : reflectFields) {
                if (f == null) {
                    continue;
                }

                f.setAccessible(true);
                if (f.isAnnotationPresent(MockInt.class)) {
                    MockInt mockInt = f.getAnnotation(MockInt.class);
                    int intValue = mockInt.value();
                    f.set(t, intValue);
                } else if (f.isAnnotationPresent(MockString.class)) {
                    MockString mockStr = f.getAnnotation(MockString.class);
                    String strValue = mockStr.value();
                    f.set(t, strValue);
                } else if (f.isAnnotationPresent(MockDouble.class)) {
                    MockDouble mockStr = f.getAnnotation(MockDouble.class);
                    double douValue = mockStr.value();
                    f.set(t, douValue);
                } else if (f.isAnnotationPresent(MockIntArray.class)) {
                    MockIntArray mockIntArr = f.getAnnotation(MockIntArray.class);
                    int[] intArrValue = mockIntArr.value();
                    f.set(t, intArrValue);
                } else if (f.isAnnotationPresent(MockStringArray.class)) {
                    MockStringArray mockStrArr = f.getAnnotation(MockStringArray.class);
                    String[] strArrValue = mockStrArr.value();
                    f.set(t, strArrValue);
                } else if (f.isAnnotationPresent(MockDoubleArray.class)) {
                    MockDoubleArray mockDouArr = f.getAnnotation(MockDoubleArray.class);
                    double[] douArrValue = mockDouArr.value();
                    f.set(t, douArrValue);
                } else if (f.isAnnotationPresent(MockObj.class)) {
                    Class<? extends IMockModel> subClass = f.getType().asSubclass(IMockModel.class);
                    T subModel = (T) subClass.newInstance();
                    Map<T, Object> map = new HashMap<>();
                    map.put(t, f);
                    mParentModel.push(map);
                    fieldAnnotationValue(subModel);
                } else if (f.isAnnotationPresent(MockObjArray.class)) {
                    MockObjArray mockObjArr = f.getAnnotation(MockObjArray.class);
                    int arrSize = mockObjArr.size();
                    boolean diffSuffix = mockObjArr.diffSuffix();
                    // 记录下需要多数组进行区分的对象
                    if (diffSuffix) {
                        mHasDiffObj.add(t);
                    }

                    // 对象型数组的class类型
                    Class arrClazz = f.getType();
                    // 对象型数组各项的class类型
                    Class itemClazz = arrClazz.getComponentType().asSubclass(IMockModel.class);
                    T itemModel = (T) itemClazz.newInstance();
                    // 需要重新构建反射的array对象，否则无法把值写入进去
                    Object objArr = Array.newInstance(itemClazz, arrSize);
                    synchronized(mArrIndex) {
                        if (!mArrIndex.containsKey(objArr)) {
                            mArrIndex.put(objArr, 0);
                        }
                    }
                    Map<T, Object> map = new HashMap<>();
                    map.put(t, objArr);
                    for (int i = 0; i < arrSize; i++) {
                        // 每次都添加进去，防止堆栈被打乱
                        mParentModel.push(map);
                        fieldAnnotationValue(itemModel);
                    }
                    // 将数组值写入到数组对象中
                    if (mHasDiffObj.contains(t)) {
                        objArr = ArrayIterator.iterator(objArr);
                        mHasDiffObj.remove(t);
                    }
                    f.set(t, objArr);

                    // 这次的轮询结束，将index的值恢复到0
                    synchronized(mArrIndex) {
                        if (mArrIndex.containsKey(objArr)) {
                            mArrIndex.remove(objArr);
                        }
                    }
                }

                // reviseFiled2Final(f);
                f.setAccessible(false);
            }

            // 将实体为对象的值写入到父实体中
            if (!mParentModel.isEmpty()) {
                Map<T, Object> parentMap = mParentModel.pop();
                for (Map.Entry<T, Object> entry : parentMap.entrySet()) {
                    T parentT = entry.getKey();
                    Object parentObj = entry.getValue();

                    if (parentObj.getClass().isArray()) {
                        synchronized(mArrIndex) {
                            int curIndex = mArrIndex.get(parentObj);
                            Array.set(parentObj, curIndex, t);
                            // 修改map的value值
                            mArrIndex.put(parentObj, curIndex + 1);
                        }
                    } else {
                        Field parentField = (Field) parentObj;
                        parentField.set(parentT, t);
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "-------start analyzer error------- \n");
            e.printStackTrace();
            Log.e(TAG, "\n-------end analyzer error-------");
        }
    }

    /**
     * 修改字段的属性为final，阻止属性值被重写
     *
     * 【经过试验，不起作用】
     *
     * @param revisedField
     *
     * @throws Exception
     */
    @Deprecated
    private void reviseFiled2Final(Field revisedField) throws Exception {
        Class ArtFieldCls = Class.forName("java.lang.reflect.ArtField");
        Object obj = ArtFieldCls.newInstance();
        Field modifiersField = ArtFieldCls.getDeclaredField("accessFlags");
        modifiersField.setAccessible(true);
        modifiersField.setInt(obj, revisedField.getModifiers() & Modifier.FINAL);
        modifiersField.setAccessible(false);
    }
}
