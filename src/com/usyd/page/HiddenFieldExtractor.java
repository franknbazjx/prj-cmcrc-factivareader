/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 *
 * @author yy
 */
public class HiddenFieldExtractor extends Extractor {

    private HashMap hash;
    public HiddenFieldExtractor(String page) {
        super(page);
        hash = new HashMap();
    }

    public List<String> getKeyList() {
        List<String> list = new ArrayList();
        Set<String> keySet = hash.keySet();
        for (String key : keySet) {
            list.add(key);
        }
        return list;
    }

    public String getValueByName(String name) {
        return (String) hash.get(name);
    }

    public void loadInput() {

        List<String> list = getByPattern("<input([^>]*)/>", page);
        for (String str : list) {
            String name = getValueByAttribute("name", str);
            String value = getValueByAttribute("value", str);
            if (name.length() > 0) {
                hash.put(name, value);
            }
        }
    }

    private String getValueByAttribute(String name, String content) {
        String value = "";
        List<String> list = getByPattern(name + "=\"([^\"]*)\"", content);
        if (list.size() > 0) {
            value = list.get(0);
        }
        return value;
    }
}
