/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication;

import bainuo.myapplication.mockserver.IMockModel;
import bainuo.myapplication.mockserver.MockInt;
import bainuo.myapplication.mockserver.MockObj;
import bainuo.myapplication.mockserver.MockObjArray;
import bainuo.myapplication.mockserver.MockString;

/**
 * Created by panhongchao on 15/12/3.
 */
public class ComplexData implements IMockModel {
    @MockInt(11)
    public int cAge;

    @MockString("fadsf")
    public String cname;

    @MockObj
    public SimpleData simpleData2;

    @MockObjArray(size = 2)
    public SimpleData[] simpleDataArr;

    @MockObjArray(size = 2, diffSuffix = true)
    public SimpleData[] simpleDataArr2;
}
