import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.Namespace;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.XMLOutputter;
import org.jdom2.output.Format;
import org.jdom2.input.sax.XMLReaders;

public class CarRental  {
	
	public static void validate_document() {
		
		try {
			
			SAXBuilder builder = new SAXBuilder();
			Document myDocument = builder.build(new File("carrental.xml"));
			
			Element carElement = myDocument.getRootElement();
			
			Namespace xsi = Namespace.getNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");
			
			carElement.addNamespaceDeclaration(xsi);
			carElement.setAttribute("noNamespaceSchemaLocation", "carrental.xsd", xsi);
			
			// XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            FileWriter writer = new FileWriter("carrental.xml");
            outputter.output(myDocument, writer);
            writer.close();
            
            SAXBuilder builder2 = new SAXBuilder(XMLReaders.XSDVALIDATING);
			Document anotherDocument = builder2.build(new File("carrental.xml"));
			System.out.println("Root: " + anotherDocument.getRootElement().getName());
			
		}
		catch(java.io.IOException e) {
            e.printStackTrace();
        }
        catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        }
	}
	
	public static void executeXSLT() {
		try {
			
			SAXBuilder builder = new SAXBuilder();
			Document myDocument = builder.build(new File("carrental.xml"));
			
			TransformerFactory tFactory = TransformerFactory.newInstance();
			
            // Make the input sources for the XML and XSLT documents
            org.jdom2.output.DOMOutputter outputter = new org.jdom2.output.DOMOutputter();
            org.w3c.dom.Document domDocument = outputter.output(myDocument);
            javax.xml.transform.Source xmlSource = new javax.xml.transform.dom.DOMSource(domDocument);
            StreamSource xsltSource = new StreamSource(new FileInputStream("example.xslt"));
            
			//Make the output result for the finished document
            StreamResult xmlResult = new StreamResult(System.out);
            
			//Get a XSLT transformer
			Transformer transformer = tFactory.newTransformer(xsltSource);
			
			//do the transform
			transformer.transform(xmlSource, xmlResult);
			
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch(TransformerConfigurationException e) {
            e.printStackTrace();
		} catch(TransformerException e) {
            e.printStackTrace();
        } catch(org.jdom2.JDOMException e) {
            e.printStackTrace();
        }
        catch(java.io.IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void list_document() {
        try {
			
			SAXBuilder builder = new SAXBuilder();
			Document myDocument = builder.build(new File("carrental.xml"));
			
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            outputter.output(myDocument, System.out);
        } 
        catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
    }
	
	public static Document reset_document() {
		
        Element carrentalElement = new Element("carrental");
        Document myDocument = new Document(carrentalElement);
        
        return myDocument;
    }
    
    public static Element askData() {
		
		Element carElement = new Element("rental");
		String input,id;
		//make
		System.out.print("Make:"); 
		input = System.console().readLine();
		id = input;
		Element make = new Element("make");
		make.addContent(input);
		carElement.addContent(make);
		
		//model
		System.out.print("Model:");
		input = System.console().readLine();
		id += input;
		Element model = new Element("model");
		model.addContent(input);
		carElement.addContent(model);
		
		//nofdays
		System.out.print("Number of days:");
		input = System.console().readLine();
		id += input + "d";
		Element nofdays = new Element("nofdays");
		nofdays.addContent(input);
		carElement.addContent(nofdays);
		
		//nofunits
		System.out.print("Number of units:");
		input = System.console().readLine();
		id += input + "u";
		Element nofunits = new Element("nofunits");
		nofunits.addContent(input);
		carElement.addContent(nofunits);
		
		//discount
		System.out.print("Discount:");
		input = System.console().readLine();
		id += input + "%";
		Element discount = new Element("discount");
		discount.addContent(input);
		carElement.addContent(discount);
		
		carElement.setAttribute(new Attribute("id",id));
		
		return carElement;
 }  

    
    public static Document new_document() {
		
		try {
		//reading carrental.xml
		SAXBuilder builder = new SAXBuilder();
        Document myDocument = builder.build(new File("carrental.xml"));
        
        //obtaining <carrental> xml element
        Element carElement = myDocument.getRootElement();
        
        //asking the user about the elements of a rental
        Element rentalElement = askData();
        
        //add a <rental> xml element to the root element (<carrental>)
        carElement.addContent(rentalElement);

        return myDocument;
		}
		
		catch(JDOMException e) {
            e.printStackTrace();
        } catch(NullPointerException e) {
            e.printStackTrace();
        } catch(java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void outputDocumentToFile(Document myDocument) {
        try {
            // XMLOutputter outputter = new XMLOutputter("  ", true);
            XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
            FileWriter writer = new FileWriter("carrental.xml");
            outputter.output(myDocument, writer);
            writer.close();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
	
	public static void main(String[] argv) {
		
		if(argv.length == 1) {
			String action = argv[0];
			
			if(action.equals("reset")) {
				outputDocumentToFile(reset_document());
				}
			else if(action.equals("new")) {
				outputDocumentToFile(new_document());
				}
			else if(action.equals("list")) {
				list_document();
				}
			else if(action.equals("xslt")) {
				executeXSLT();
				}
			else if(action.equals("validate")) {
				validate_document();
				}
						
			else { //usage
				System.out.println(action + " is not a valid option.");
				System.out.println("Usage: CarRental [option] \n where option is one of the following:");
				System.out.println("  reset  ");
				System.out.println("  new  ");
				System.out.println("  list  ");
				System.out.println("  xslt  ");
				System.out.println("  validate  ");
				} 
			 }
			 
			 else {	//usage
				System.out.println("Usage: CarRental [option] \n where option is one of the following:");
				System.out.println("  reset  ");
				System.out.println("  new  ");
				System.out.println("  list  ");
				System.out.println("  xslt  "); 
				System.out.println("  validate  ");
				 }	
			 
				
		}
	
	
	
	
	
	
	
	
	
	
	
	
	}
