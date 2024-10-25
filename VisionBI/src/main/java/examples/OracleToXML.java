package examples;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class OracleToXML {
    public static void main(String[] args) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");

            // 2. Connect to the database
            conn = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "username", "password");

            // 3. Create a statement
            stmt = conn.createStatement();

            // 4. Execute the query
            String query = "SELECT * FROM your_table_name";
            rs = stmt.executeQuery(query);

            // 5. Initialize the XML Document
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.newDocument();

            // 6. Create the root element
            Element rootElement = doc.createElement("Rows");
            doc.appendChild(rootElement);

            // 7. Process each row in the ResultSet
            while (rs.next()) {
                Element row = doc.createElement("Row");
                rootElement.appendChild(row);

                // Assume table columns are column1, column2, etc.
                Element column1 = doc.createElement("Column1");
                column1.appendChild(doc.createTextNode(rs.getString("column1")));
                row.appendChild(column1);

                Element column2 = doc.createElement("Column2");
                column2.appendChild(doc.createTextNode(rs.getString("column2")));
                row.appendChild(column2);

                // Repeat for other columns as needed
            }

            // 8. Convert the document to XML format
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);

            // 9. Output the XML to console or file
            StreamResult consoleResult = new StreamResult(System.out);
            transformer.transform(source, consoleResult);

            // To write to a file, uncomment the following lines
            // StreamResult fileResult = new StreamResult(new File("output.xml"));
            // transformer.transform(source, fileResult);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
