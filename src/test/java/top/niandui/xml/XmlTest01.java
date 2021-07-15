package top.niandui.xml;

import com.sun.deploy.xml.XMLNode;
import com.sun.javaws.jnl.XMLFormat;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Xml测试
 *
 * @author liyongda
 * @version 1.0
 * @date 2021/7/7 9:05
 */
public class XmlTest01 {
    String xml = "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\"><soap:Header/><soap:Body><LST_CAPResponse xmlns=\"http://www.chinaunicom.com/HSS/\"><Result><ResultCode>0</ResultCode><ResultDesc>SUCCESS0001:Operation is successful</ResultDesc><ResultData><MCAP>9</MCAP></ResultData></Result></LST_CAPResponse></soap:Body></soap:Envelope>";

    /**
     * 最终能够实现的
     * @throws Exception
     */
    @Test
    public void xmlFormat() throws Exception {
        XMLNode xmlNode = XMLFormat.parseBits(xml.getBytes());
        System.out.println(xmlNode);
    }

    @Test
    public void xmlFormat01() throws Exception {

        Source xmlSource = new StreamSource(new StringReader(xml));
        StringWriter stringWriter = new StringWriter();
        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
            transformer.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlSource, new StreamResult(stringWriter));

            System.out.println(stringWriter.toString().trim());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    public void xmlFormat02() throws Exception {
//        new SAXReader
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        Document document = db.parse(is);

        OutputFormat format = new OutputFormat(document);
//        format.setLineWidth(65);
        format.setIndenting(true);
        format.setIndent(2);
        Writer out = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(out, format);
        serializer.serialize(document);

        System.out.println(out);
    }

    @Test
    public void xmlFormat03() throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new InputSource(new StringReader(xml)));

        DOMSource src = new DOMSource(doc);
        //  StreamResult sr = new StreamResult(System.out);
        //  StreamResult sr = new StreamResult(new File(fileName));
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer t = tf.newTransformer();
        t.setOutputProperty(OutputKeys.METHOD, "xml");
        t.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
        t.setOutputProperty(OutputKeys.INDENT, "yes");
        t.setOutputProperty("{https://xml.apache.org/xslt}indent-amount", "2");

        StringWriter stringWriter = new StringWriter();
        StreamResult sr = new StreamResult(stringWriter);
        t.transform(src, sr);

        System.out.println(stringWriter);
    }
}
