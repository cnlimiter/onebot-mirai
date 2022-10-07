package cn.evolvefield.mirai.onebot.entity;

import java.io.*;

/**
 * Description:
 * Author: cnlimiter
 * Date: 2022/10/7 19:53
 * Version: 1.0
 */
public class MessageChainOrString implements Externalizable {

    boolean isString;
    String a;

    public MessageChainOrString(boolean isString){
        this.isString = isString;
    }


    @Override
    public void writeExternal(ObjectOutput out) throws IOException {

    }

    @Override
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

    }
}
