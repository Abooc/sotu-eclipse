package com.abooc.sotu;

import android.util.Log;

import com.abooc.util.Debug;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private static boolean isSimpleName = true;
    @Test
    public void addition_isCorrect() throws Exception {
        d(this);
        d("OK");
    }



    public void d(Object object) {
        StackTraceElement[] stack = new Exception().getStackTrace();
        String className = stack[1].getClassName();
        className = className.substring(className.lastIndexOf(".") + 1);
        String stackString = Stacker.stackString(object, stack, isSimpleName);

        System.out.println(stackString);
    }

    @Test
    public void debug() {
    }
}


class Stacker {

    public static String mMessageHeader = ":\n";
    public static String mMessageFooter = "";

    /**
     * @param cls          class Or Object
     * @param stack        A representation of a single stack frame.
     * @param isSimpleName 是否简化输出
     * @return 返回可用于打印的字符
     */
    protected static String stackString(Object cls, StackTraceElement[] stack, boolean isSimpleName) {
        String me = stack[0].getMethodName();
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        int size = stack.length;
        for (; i < size; i++) {
            if (!me.equals(stack[i].getMethodName())) {
                stringBuffer.append(stack[i].toString() + mMessageHeader   //第一行 + ':'
                        + cls);                                 //打印内容
                break;
            }
        }

        if (isSimpleName) {
            String className = stack[i].getClassName();
            stringBuffer.delete(0, className.lastIndexOf(".") + 1);
        }
        return stringBuffer.toString() + mMessageFooter;
    }

    /**
     * @hide
     */
    private static String toStackTraceString(Object cls) {
        StackTraceElement[] stack = new Exception().getStackTrace();
        return stack[1].toString() + ", " + cls.toString()
                + mMessageFooter;
    }
}
