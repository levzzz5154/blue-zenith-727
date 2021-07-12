package cat.module.value;

import cat.module.Module;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SettingManager {
    LinkedList<Value<?>> values = new LinkedList<>();
    public void initModule(Value<?>[] a){
        values.addAll(Arrays.asList(a));
    }
    public List<Value<?>> getValuesByModule(Module mod){
        List<Value<?>> s = new LinkedList<>();
        for (Value<?> m : values) {
            if(m.parentModule == mod){
                s.add(m);
            }
        }
        return s;
    }
    public List<Value<?>> getValueByKey(String key){
        List<Value<?>> s = new LinkedList<>();
        for (Value<?> m : values) {
            if(m.id.equals(key)){
                s.add(m);
            }
        }
        return s;
    }
}
