/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dbms;

import java.io.File;


import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Amin
 */
public class XMLBuilder {

    void createBankUsers(String username, String PASS_HASH) {
        try {
            File f = new File(Config.bankUsers);
            if (!f.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element root = doc.createElement("bankusers");
                doc.appendChild(root);
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                DOMSource source = new DOMSource(doc);
                trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                PrintWriter pw = new PrintWriter(new FileOutputStream(Config.bankUsers));
                StreamResult result = new StreamResult(pw);
                trans.transform(source, result);


            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(f);
            Element root = doc.getDocumentElement();
            Element user = doc.createElement("user");
            Element user_name = doc.createElement("username");
            user_name.setTextContent(username);
            Element pass_hash = doc.createElement("password");
            pass_hash.setTextContent(PASS_HASH);
            root.appendChild(user);
            user.appendChild(user_name);
            user.appendChild(pass_hash);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            OutputStream f0;
            byte buf[] = xmlString.getBytes();
            f0 = new FileOutputStream(Config.bankUsers);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();
            buf = null;


        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    int CheckLogin(String username, String password) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(Config.bankUsers));
            doc.getDocumentElement().normalize();
            NodeList users = doc.getElementsByTagName("user");
            for (int i = 0; i < users.getLength(); i++) {
                Node user_node = users.item(i);
                if (user_node.getNodeType() == Node.ELEMENT_NODE) {
                    Element userElement = (Element) user_node;
                    if (username.compareTo(getTagValue("username", userElement)) == 0) {
                        if (getTagValue("password", userElement).compareTo(MD5_Hash.MD5(password)) == 0) {
                            //username and password are correct
                            return i + 1;
                        }
                    }
                }
            }
            //username and password are not correct
            return 0;
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
            //exception return
            return -1;
        }
    }

    private static String getTagValue(String Tag, Element eElement) {
        NodeList nList = eElement.getElementsByTagName(Tag).item(0).getChildNodes();
        Node nValue = (Node) nList.item(0);

        return nValue.getNodeValue();
    }

    public void CreateLog(String username, String action) {
        try {
            File f = new File(Config.logfile);
            Calendar now = Calendar.getInstance();
            String time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
            String date = now.get(Calendar.DAY_OF_MONTH) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.YEAR);
            if (!f.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element root = doc.createElement("logger");
                doc.appendChild(root);
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                DOMSource source = new DOMSource(doc);
                trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                PrintWriter pw = new PrintWriter(new FileOutputStream(Config.logfile));
                StreamResult result = new StreamResult(pw);
                trans.transform(source, result);


            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(f);
            Element root = doc.getDocumentElement();
            Element log = doc.createElement("log");
            Element user_name = doc.createElement("username");
            user_name.setTextContent(username);
            Element actionE = doc.createElement("Action");
            actionE.setTextContent(action);
            Element dateE = doc.createElement("Date");
            dateE.setTextContent(date);
            Element timeE = doc.createElement("Time");
            timeE.setTextContent(time);
            root.appendChild(log);
            log.appendChild(actionE);
            log.appendChild(user_name);
            log.appendChild(dateE);
            log.appendChild(timeE);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            OutputStream f0;
            byte buf[] = xmlString.getBytes();
            f0 = new FileOutputStream(Config.logfile);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();
            buf = null;


        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public int ChangePassword(int index, String username, String OLDpassword, String NEWpassword) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(new File(Config.bankUsers));
            doc.getDocumentElement().normalize();
            NodeList users = doc.getElementsByTagName("user");


            Node user_node = users.item(index);
            if (user_node.getNodeType() == Node.ELEMENT_NODE) {
                Element userElement = (Element) user_node;
                if (MD5_Hash.MD5(OLDpassword).compareTo(getTagValue("password", userElement)) == 0) {
                    NodeList childs = userElement.getChildNodes();
                    Element passE = (Element) childs.item(1);
                    if (passE.getTagName().compareTo("password") == 0) {
                        passE.setTextContent(MD5_Hash.MD5(NEWpassword));
                        TransformerFactory transfac = TransformerFactory.newInstance();
                        Transformer trans = transfac.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        PrintWriter pw = new PrintWriter(new FileOutputStream(Config.bankUsers));
                        StreamResult result = new StreamResult(pw);
                        trans.transform(source, result);
                        return 1;
                    }
                }
            }
            return 0;


        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
    }

    public void CreateTable(LinkedList<String> command, String username) {
        //create empty table
        Calendar now = Calendar.getInstance();
        String time = now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE) + ":" + now.get(Calendar.SECOND);
        String date = now.get(Calendar.DAY_OF_MONTH) + "-" + now.get(Calendar.MONTH) + "-" + now.get(Calendar.YEAR);
        String address = "";
        try {
            address = "c:\\" + command.get(2) + ".xml";
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement(command.get(2));
            doc.appendChild(root);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            DOMSource source = new DOMSource(doc);
            trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            PrintWriter pw = new PrintWriter(new FileOutputStream(address));
            StreamResult result = new StreamResult(pw);
            trans.transform(source, result);
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        //create table schema
        try {

            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();
            Element root = doc.createElement(command.get(2) + "schema");
            Element user_name = doc.createElement("username");
            user_name.setTextContent(username);
            root.appendChild(user_name);
            Element dateE = doc.createElement("date");
            dateE.setTextContent(date);
            root.appendChild(dateE);
            Element timeE = doc.createElement("time");
            timeE.setTextContent(time);
            root.appendChild(timeE);
            Element addressE = doc.createElement("tableAddress");
            addressE.setTextContent(address);
            root.appendChild(addressE);
            Element nameE = doc.createElement("tableName");
            nameE.setTextContent(command.get(2));
            root.appendChild(nameE);
            //create fields
            Element fieldE = doc.createElement("field");
            Element fieldName = doc.createElement("name");
            Element fieldType = doc.createElement("type");
            fieldName.setTextContent("flag");
            fieldType.setTextContent("int");
            fieldE.appendChild(fieldName);
            fieldE.appendChild(fieldType);
            root.appendChild(fieldE);
            command.add(",");

            int fieldCount = 1;
            int i = 3;
            while (i < command.size()) {
                fieldE = doc.createElement("field");
                fieldName = doc.createElement("name");
                fieldType = doc.createElement("type");
                if (command.get(i + 1).compareTo(",") == 0) {
                    fieldName.setTextContent(command.get(i));
                    fieldType.setTextContent("int");
                    i = i + 2;
                } else {
                    fieldName.setTextContent(command.get(i));
                    fieldType.setTextContent(command.get(i + 1));
                    i = i + 3;
                }
                fieldE.appendChild(fieldName);
                fieldE.appendChild(fieldType);
                root.appendChild(fieldE);
                fieldCount++;
            }

            Element rec = doc.createElement("records");
            rec.setTextContent("0");
            Element activeRec = doc.createElement("activeRecords");
            activeRec.setTextContent("0");
            Element nonActiveRec = doc.createElement("nonActiveRecords");
            nonActiveRec.setTextContent("0");
            root.appendChild(rec);
            root.appendChild(activeRec);
            root.appendChild(nonActiveRec);
            doc.appendChild(root);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            DOMSource source = new DOMSource(doc);
            trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            address = "c:\\" + command.get(2) + "Schema.xml";
            PrintWriter pw = new PrintWriter(new FileOutputStream(address));
            StreamResult result = new StreamResult(pw);
            trans.transform(source, result);
            updateDatabesSchema(command.get(2), fieldCount);

        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void updateDatabesSchema(String tablename, int count) {
        try {
            File f = new File(Config.DBSchema);
            if (!f.exists()) {
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.newDocument();
                Element root = doc.createElement("DBSchema");
                doc.appendChild(root);
                TransformerFactory transfac = TransformerFactory.newInstance();
                Transformer trans = transfac.newTransformer();
                DOMSource source = new DOMSource(doc);
                trans.setOutputProperty(OutputKeys.ENCODING, "utf-8");
                trans.setOutputProperty(OutputKeys.INDENT, "yes");
                PrintWriter pw = new PrintWriter(new FileOutputStream(Config.DBSchema));
                StreamResult result = new StreamResult(pw);
                trans.transform(source, result);


            }
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(f);
            Element root = doc.getDocumentElement();
            Element table = doc.createElement("table");
            Element table_name = doc.createElement("name");
            table_name.setTextContent(tablename);
            Element fieldCount = doc.createElement("fieldCount");
            fieldCount.setTextContent(String.valueOf(count));
            table.appendChild(table_name);
            table.appendChild(fieldCount);
            root.appendChild(table);
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            StringWriter sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            trans.transform(source, result);
            String xmlString = sw.toString();
            OutputStream f0;
            byte buf[] = xmlString.getBytes();
            f0 = new FileOutputStream(Config.DBSchema);
            for (int i = 0; i < buf.length; i++) {
                f0.write(buf[i]);
            }
            f0.close();
            buf = null;


        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void diplayLog() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Config.logfile);
            NodeList logs = doc.getElementsByTagName("log");
            for (int i = 0; i < logs.getLength(); i++) {
                Element log = (Element) logs.item(i);
                System.out.println("--------------------------------");
                System.out.println("Action = " + getTagValue("Action", log));
                System.out.println("username = " + getTagValue("username", log));
                System.out.println("Date = " + getTagValue("Date", log));
                System.out.println("Time = " + getTagValue("Time", log));

            }
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayTable(String tablename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("c:\\" + tablename + "Schema.xml");
            NodeList fields = doc.getElementsByTagName("field");
            NodeList nodes = doc.getElementsByTagName("username");
            Element node = (Element) nodes.item(0);
            System.out.println("################################");
            System.out.println("################################");
            System.out.println("################################");
            System.out.println("--------------------------------");
            System.out.println("username = " + node.getTextContent());

            nodes = doc.getElementsByTagName("date");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("Date = " + node.getTextContent());

            nodes = doc.getElementsByTagName("time");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("Time = " + node.getTextContent());

            nodes = doc.getElementsByTagName("tableAddress");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("Table Address = " + node.getTextContent());

            nodes = doc.getElementsByTagName("tableName");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("Table Name = " + node.getTextContent());

            nodes = doc.getElementsByTagName("records");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("number of records = " + node.getTextContent());

            nodes = doc.getElementsByTagName("activeRecords");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("number of active records = " + node.getTextContent());

            nodes = doc.getElementsByTagName("nonActiveRecords");
            node = (Element) nodes.item(0);
            System.out.println("--------------------------------");
            System.out.println("number of non-active records = " + node.getTextContent());

            for (int i = 0; i < fields.getLength(); i++) {
                Element field = (Element) fields.item(i);
                System.out.println("--------------------------------");
                System.out.println("field name = " + getTagValue("name", field));
                System.out.println("field type = " + getTagValue("type", field));

            }
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void displayCatalogue() {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(Config.DBSchema);
            NodeList nodes = doc.getElementsByTagName("table");
            for (int i = 0; i < nodes.getLength(); i++) {
                Element node = (Element) nodes.item(i);
                String tablename = getTagValue("name", node);
                this.displayTable(tablename);
            }
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void load(String tablename, LinkedList<String> readfile) {
        int fieldCount = 0;
        try {
            /**
             * DocumentBuilderFactory docFactory =
             * DocumentBuilderFactory.newInstance(); DocumentBuilder docBuilder
             * = docFactory.newDocumentBuilder(); Document doc =
             * docBuilder.parse(Config.DBSchema); NodeList tables =
             * doc.getElementsByTagName("table"); for(int
             * i=0;i<tables.getLength();i++){ Element table =
             * (Element)tables.item(i);
             * if(tablename.compareTo(getTagValue("name", table))==0){
             * fieldCount = Integer.parseInt(getTagValue("fieldCount", table));
             * } }
             */
            DocumentBuilderFactory tableFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder tableBuilder = tableFactory.newDocumentBuilder();
            Document tabledoc = tableBuilder.parse("c:\\" + tablename + ".xml");
            DocumentBuilderFactory schemaFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder schemaBuilder = schemaFactory.newDocumentBuilder();
            Document schemadoc = schemaBuilder.parse("c:\\" + tablename + "Schema.xml");
            Element tableRoot = tabledoc.getDocumentElement();
            NodeList fields = schemadoc.getElementsByTagName("field");
            fieldCount = fields.getLength();
            int records = (readfile.size()) / (fieldCount - 1);
            for (int j = 0; j < records; j++) {
                Element row = tabledoc.createElement("row");
                Element field;
                Element tableE = tabledoc.createElement("flag");
                tableE.setTextContent("1");
                row.appendChild(tableE);
                for (int k = 1; k < fieldCount; k++) {
                    field = (Element) fields.item(k);
                    tableE = tabledoc.createElement(getTagValue("name", field));
                    tableE.setTextContent(readfile.poll());
                    row.appendChild(tableE);
                }
                tableRoot.appendChild(row);
            }
            NodeList recs = schemadoc.getElementsByTagName("records");
            Element rec = (Element) recs.item(0);
            rec.setTextContent(String.valueOf(records));
            NodeList arecs = schemadoc.getElementsByTagName("activeRecords");
            Element arec = (Element) arecs.item(0);
            arec.setTextContent(String.valueOf(records));
            //save table
            TransformerFactory transfac = TransformerFactory.newInstance();
            Transformer trans = transfac.newTransformer();
            DOMSource source = new DOMSource(tabledoc);
            PrintWriter pw = new PrintWriter(new FileOutputStream("c:\\" + tablename + ".xml"));
            StreamResult result = new StreamResult(pw);
            trans.transform(source, result);
            //save schema
            source = new DOMSource(schemadoc);
            pw = new PrintWriter(new FileOutputStream("c:\\" + tablename + "Schema.xml"));
            result = new StreamResult(pw);
            trans.transform(source, result);

        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selAll(String tablename) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("c:\\" + tablename + ".xml");
            NodeList rows = doc.getElementsByTagName("row");
            int count = rows.item(0).getChildNodes().getLength();
            for (int k = 0; k < count; k++) {
                System.out.printf("%20s", rows.item(0).getChildNodes().item(k).getNodeName());


            }
            System.out.println();
            System.out.println();

            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList childs = row.getChildNodes();
                for (int j = 0; j < row.getChildNodes().getLength(); j++) {
                    Element child = (Element) childs.item(j);
                    System.out.printf("%20s", getTagValue(child.getTagName(), row));


                }
                System.out.println();
            }
        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selWhere(LinkedList<String> command) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("c:\\" + command.get(3) + ".xml");
            NodeList rows = doc.getElementsByTagName("row");
            int count = rows.item(0).getChildNodes().getLength();
            for (int k = 0; k < count; k++) {
                System.out.printf("%20s", rows.item(0).getChildNodes().item(k).getNodeName());


            }
            System.out.println();
            System.out.println();
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList childs = row.getChildNodes();
                int c = 0;
                for (int j = 0; j < row.getChildNodes().getLength(); j++) {
                    Element child = (Element) childs.item(j);
                    c = 0;
                    try {
                        int value = Integer.parseInt(command.get(7));
                        if (command.get(6).compareTo("=") == 0) {
                            if (value == Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("!=") == 0) {
                            if (value != Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("<=") == 0) {
                            if (value >= Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo(">=") == 0) {
                            if (value <= Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo(">") == 0) {
                            if (value < Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("<") == 0) {
                            if (value > Integer.parseInt(getTagValue(command.get(5), row))) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        String value = command.get(7);
                        if (command.get(6).compareTo("=") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) == 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("!=") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) != 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("<=") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) >= 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo(">=") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) <= 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo(">") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) < 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(6).compareTo("<") == 0) {
                            if (value.compareTo(getTagValue(command.get(5), row)) > 0) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                    }
                    //Element child = (Element)childs.item(j);
                    //System.out.printf("%20s", getTagValue(child.getTagName(),row));
                    //if(getTagValue(command.get(5), row))

                }
                if (c == 1) {
                    System.out.println();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selFieldwhere(LinkedList<String> command, LinkedList<String> fields) {
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse("c:\\" + command.get(command.size() - 5) + ".xml");
            NodeList rows = doc.getElementsByTagName("row");
            int count = rows.item(0).getChildNodes().getLength();

            for (int k = 0; k < count; k++) {
                if (fields.contains(rows.item(0).getChildNodes().item(k).getNodeName())) {
                    System.out.printf("%20s", rows.item(0).getChildNodes().item(k).getNodeName());
                }


            }


            System.out.println();
            System.out.println();
            for (int i = 0; i < rows.getLength(); i++) {
                Element row = (Element) rows.item(i);
                NodeList childs = row.getChildNodes();
                int c = 0;
                for (int j = 0; j < row.getChildNodes().getLength(); j++) {
                    Element child = (Element) childs.item(j);

                    try {
                        int value = Integer.parseInt(command.get(command.size() - 1));
                        if (command.get(command.size() - 2).compareTo("=") == 0) {
                            if (value == Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("!=") == 0) {
                            if (value != Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("<=") == 0) {
                            if (value >= Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo(">=") == 0) {
                            if (value <= Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo(">") == 0) {
                            if (value < Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("<") == 0) {
                            if (value > Integer.parseInt(getTagValue(command.get(command.size() - 3), row)) && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                    } catch (NumberFormatException ex) {
                        String value = command.get(command.size() - 1);
                        if (command.get(command.size() - 2).compareTo("=") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) == 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("!=") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) != 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("<=") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) >= 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo(">=") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) <= 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo(">") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) < 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                        if (command.get(command.size() - 2).compareTo("<") == 0) {
                            if (value.compareTo(getTagValue(command.get(command.size() - 3), row)) > 0 && fields.contains(child.getTagName())) {
                                System.out.printf("%20s", getTagValue(child.getTagName(), row));
                                c = 1;
                            }
                        }
                    }
                    //Element child = (Element)childs.item(j);
                    //System.out.printf("%20s", getTagValue(child.getTagName(),row));
                    //if(getTagValue(command.get(5), row))

                }
                if (c == 1) {
                    System.out.println();
                }
            }

        } catch (Exception ex) {
            Logger.getLogger(XMLBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
