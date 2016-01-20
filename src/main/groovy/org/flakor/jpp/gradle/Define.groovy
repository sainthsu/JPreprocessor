package org.flakor.jpp.gradle

/**
 * Created by xusq on 2016/1/20.
 */
class Define {
    private String name;
    private String value;

    public Define(String n,String v) {
        name = n
        value = v
    }

    void setName(String name) {
        this.name = name
    }

    void setValue(String value) {
        this.value = value
    }
}
