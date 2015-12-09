/*
 * Copyright (C) 2015 Baidu, Inc. All Rights Reserved.
 */
package bainuo.myapplication;

import bainuo.myapplication.mockserver.IMockModel;
import bainuo.myapplication.mockserver.MockObj;
import bainuo.myapplication.mockserver.MockObjArray;

/**
 * Created by panhongchao on 15/12/4.
 */
public class MoreComplexData implements IMockModel {
    @MockObj
    public ComplexData data;

    @MockObjArray(size = 2, diffSuffix = true)
    public ComplexData[] array;

    @MockObjArray(size = 3)
    public ComplexData[] array2;
}
