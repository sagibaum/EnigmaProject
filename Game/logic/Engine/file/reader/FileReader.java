package file.reader;

import schem.out.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class FileReader {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "schem.out";

    public FileReader() {
    }
    private CTEEnigma deserializeFrom(InputStream inputStream) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (CTEEnigma) u.unmarshal(inputStream);
    }

    public CTEEnigma readFile(String filePath)
    {
        try {
            InputStream inputStream = new FileInputStream(filePath);
            return deserializeFrom(inputStream);
        }
        catch (FileNotFoundException e) {
            //file not found problem throw
            throw new Exceptions.InvalidXmlFileException("File wasn't found in this current path!");
        }
        catch( JAXBException e ){
          //problem with input stream
            throw new  Exceptions.InvalidXmlFileException(e.getCause().getMessage());
        }
    }

}
