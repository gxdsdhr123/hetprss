/**
 *application name:prss
 *application describing:this class handles the request of the client
 *copyright:Copyright(c) 2017 Neusoft LTD.
 *company:Neusoft
 *time:2017年9月1日 上午9:28:02
 *@author:baochl
 *@version:[v1.0]
 */
package com.neusoft.framework.common.utils;

import java.lang.reflect.Field;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.neusoft.framework.common.annotation.Column;

public class AnnotationUtils {
    private static Logger logger = LoggerFactory.getLogger(AnnotationUtils.class);
    /**
     * 
     *Discription:根据属性获取属性对应列注解.
     *@param cls
     *@param fieldName 属性名
     *@return
     *@return:返回值意义
     *@author:baochl
     *@update:2017年9月1日 baochl [变更描述]
     */
    public static Column getColumn(Class<?> cls,String fieldName) {
        try {
            Field field = cls.getDeclaredField(fieldName);
            if (field != null && field.isAnnotationPresent(Column.class)) {
                return field.getAnnotation(Column.class);
            }
        } catch (NoSuchFieldException | SecurityException e) {
            logger.error(e.toString());
            return null;
        }
        return null;
    }
}
