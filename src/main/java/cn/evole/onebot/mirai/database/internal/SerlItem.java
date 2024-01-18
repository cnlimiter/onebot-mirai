package cn.evole.onebot.mirai.database.internal;

import java.io.Serial;
import java.io.Serializable;

class SerlItem implements Serializable {
    @Serial
    private static final long serialVersionUID = 11451419196666L;
    public Object[] key;
    public Object[] value;

    protected SerlItem(Object[] k, Object[] v) {
        key = k;
        value = v;
    }
}
