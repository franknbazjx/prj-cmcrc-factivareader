/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.usyd.page;

import com.usyd.log.Logger;
import com.usyd.unit.CompanyUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 * @author yy
 */
public class CompanyNameExtractor extends Extractor {

    public CompanyNameExtractor(String page) {
        super(page);
    }

    public CompanyUnit loadCompanyName(String company, boolean fuzzy) {
        //  mnuItm_click(this,'{30:0,5:&quot;ACARES&quot;,29:0,28:&quot;ACACIA RESOURCES LIMITED&quot;,33:0,21:0,20:0}')
        List<String> list = getByPattern("mnuItm_click(.*)", page);
        List<CompanyUnit> colist = new ArrayList<CompanyUnit>();
        for (String line : list) {
            List<String> names = getByPattern("&quot;([^&]*)&quot;", line);
            if (names.size() > 1) {
                String shortName = names.get(0);
                String fullName = names.get(1);
                CompanyUnit unit = new CompanyUnit(shortName, fullName, company);
                colist.add(unit);
            }
        }

        if (colist.size() > 0) {

            Collections.sort(colist);
            CompanyUnit unit = colist.get(0);
            if ( !fuzzy && unit.getRatio() > 0.6) {
                Logger.log("DROPPED: " + unit.getSearchName().trim() + " != " + unit.getFullName() + "\n" );
                return null;
            } else {
                return unit;
            }
        } else {
            return null;
        }
    }
}
