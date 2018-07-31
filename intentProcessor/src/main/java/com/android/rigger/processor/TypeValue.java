package com.android.rigger.processor;

import com.rigger.android.annotation.IntentValue;
import com.squareup.javapoet.TypeName;

/**
 * Created by Edgar on 2018/7/31.
 */
public abstract class TypeValue {

    abstract Object value(IntentValue value);

    private static class IntValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defInt();
        }
    }

    private static class BolValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defBol();
        }
    }

    private static class LongValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defLong();
        }
    }

    private static class ShortValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defShort();
        }
    }

    private static class ByteValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defByte();
        }
    }

    private static class FloatValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defFloat();
        }
    }

    private static class DoubleValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defDouble();
        }
    }

    private static class StringValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defString();
        }
    }

    private static class CharValue extends TypeValue {

        @Override
        public Object value(IntentValue value) {
            return value.defChar();
        }
    }

    public static class Factory {

        public static TypeValue createTypeValue(TypeName typeName) {
            if (typeName == TypeName.BYTE) {
                return new ByteValue();
            } else if (typeName == TypeName.INT) {
                return new IntValue();
            } else if (typeName == TypeName.BOOLEAN) {
                return new BolValue();
            } else if (typeName == TypeName.SHORT) {
                return new ShortValue();
            } else if (typeName == TypeName.LONG) {
                return new LongValue();
            } else if (typeName == TypeName.FLOAT) {
                return new FloatValue();
            } else if (typeName == TypeName.DOUBLE) {
                return new DoubleValue();
            } else if (typeName == TypeName.CHAR) {
                return new CharValue();
            } else if (typeName == InjectSet.STRING_TYPE) {
                return new StringValue();
            } else {
                return null;
            }
        }
    }
}
