package com.arquillian.tomcat.managed;

import java.io.File;
import java.io.IOException;

import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.OperateOnDeployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.container.test.api.TargetsContainer;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

import com.meterware.httpunit.GetMethodWebRequest;
import com.meterware.httpunit.WebConversation;

@RunAsClient
@RunWith(Arquillian.class)
public class TestArquillianTomcatManaged {

	@ArquillianResource
	private ContainerController contoller;
	
	@ArquillianResource
	private Deployer deployer;
	
	@TargetsContainer("node-0")
	@Deployment(managed=false, name="deploy-0")
	public static Archive<WebArchive> deployWarFile(){
		String filePath = new File("sample.war").getAbsolutePath();
		System.out.println(filePath);
		WebArchive arc = ShrinkWrap.createFromZipFile(WebArchive.class, new File("sample.war"));
		return arc;
	}
	
	
	@Test
	@InSequence(-1)
	public void startServers(){
		contoller.start("node-0");
		deployer.deploy("deploy-0");
	}
	
	@Test
	@OperateOnDeployment("deploy-0")
	public void testSampleArquillian() throws IOException, SAXException{
		WebConversation wc = new WebConversation();
		GetMethodWebRequest get = new GetMethodWebRequest("http://localhost:8080/sample/hello.jsp");
		System.out.println(wc.getResponse(get).getText());
	}
	
	
	@Test
	public void testRuntTimeController() throws IOException, SAXException{
		
		/*Map<String, String> config = new HashMap<String, String>();
		config.put("catalinaHome", "G:/Restart/arquillian-tomcat-embedded-7/tomcat");
		contoller.start("tomcat-managed-7", config);
		WebConversation wc = new WebConversation();
		GetMethodWebRequest get = new GetMethodWebRequest("http://localhost:8080/sample/hello.jsp");
		System.out.println(wc.getResponse(get).getText());*/
	}
}
