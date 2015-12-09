/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication;

import bainuo.myapplication.mockserver.IMockModel;
import bainuo.myapplication.mockserver.MockDouble;
import bainuo.myapplication.mockserver.MockDoubleArray;
import bainuo.myapplication.mockserver.MockInt;
import bainuo.myapplication.mockserver.MockIntArray;
import bainuo.myapplication.mockserver.MockString;
import bainuo.myapplication.mockserver.MockStringArray;

/**
 * Created by panhongchao on 15/12/3.
 */
public class SimpleData implements IMockModel {
    @MockString("pan")
    public String name;

    @MockInt(18)
    public int age;

    @MockInt(11)
    public Integer age2;

    @MockStringArray({"pan1", "pan2"})
    public String[] oldNames;

    @MockIntArray({1, 2, 3})
    public int[] oldages;

    @MockDouble(11.11)
    public double dvalue;

    @MockDoubleArray({11.11, 12.1})
    public double[] dvalueArr;
}
