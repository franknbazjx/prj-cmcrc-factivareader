/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package xmlparser;

import com.usyd.util.FileLoader;
import java.io.File;
import junit.framework.TestCase;

/**
 *
 * @author yangyang
 */
public class XmlParserTest extends TestCase {

    File file;

    @Override
    protected void setUp() throws Exception {
        file = new File("x.xml");
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testXMLParser() throws Exception{
        FileLoader.parseXml(file);
    }

    // TODO add test methods here. The name must begin with 'test'. For example:
    // public void testHello() {}

}
