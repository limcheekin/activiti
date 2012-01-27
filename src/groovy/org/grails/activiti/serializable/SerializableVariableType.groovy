package org.grails.activiti.serializable

import org.activiti.engine.ActivitiException
import org.activiti.engine.impl.persistence.entity.VariableInstanceEntity
import org.activiti.engine.impl.variable.ByteArrayType
import org.activiti.engine.impl.variable.ValueFields
import org.activiti.engine.impl.util.IoUtil
import org.activiti.engine.impl.context.Context
import org.grails.activiti.serializable.GroovyObjectInputStream

/**
 *
 * @author <a href='mailto:pais@exigenservices.com'>Ilya Drabenia</a>
 *
 * @since 5.8.2
 */
class SerializableVariableType extends ByteArrayType {
    public static final String TYPE_NAME = "groovySerializable"

    private static final long serialVersionUID = 1L
    
    String getTypeName() {
        return TYPE_NAME;
    }

    Object getValue(ValueFields valueFields) {
        Object cachedObject = valueFields.getCachedValue()
        if (cachedObject != null) {
            return cachedObject
        }
        byte[] bytes = (byte[]) super.getValue(valueFields)
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes)
        Object deserializedObject = null
        try {
            GroovyObjectInputStream ois = new GroovyObjectInputStream(bais, Thread.currentThread().contextClassLoader)
            deserializedObject = ois.readObject()
            valueFields.setCachedValue(deserializedObject)

            if (valueFields instanceof VariableInstanceEntity) {
                Context
                        .getCommandContext()
                        .getDbSqlSession()
                        .addDeserializedObject(deserializedObject, bytes, (VariableInstanceEntity) valueFields)
            }

        }
        catch (Exception e) {
            throw new ActivitiException("coudn't deserialize object in variable '" + valueFields.getName() + "'", e)
        }
        finally {
            IoUtil.closeSilently(bais)
        }
        return deserializedObject
    }

    void setValue(Object value, ValueFields valueFields) {
        byte[] byteArray = serialize(value, valueFields)
        valueFields.setCachedValue(value)
        super.setValue(byteArray, valueFields)
    }

    static byte[] serialize(Object value, ValueFields valueFields) {
        if (value == null) {
            return null
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream()
        ObjectOutputStream ois = null
        try {
            ois = new ObjectOutputStream(baos)
            ois.writeObject(value)
        }
        catch (Exception e) {
            throw new ActivitiException("coudn't deserialize value '" + value + "' in variable '" + valueFields.getName() + "'", e)
        }
        finally {
            IoUtil.closeSilently(ois)
        }
        return baos.toByteArray()
    }

    boolean isAbleToStore(Object value) {
        return value instanceof Serializable;
    }
}


